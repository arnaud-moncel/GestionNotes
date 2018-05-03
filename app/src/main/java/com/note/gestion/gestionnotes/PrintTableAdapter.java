package com.note.gestion.gestionnotes;

import android.arch.persistence.room.util.StringUtil;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;

import com.itextpdf.text.pdf.PdfPTable;
import com.note.gestion.table.TableDish;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

public class PrintTableAdapter extends PrintDocumentAdapter {
    private PrintedPdfDocument m_pdfDocument;
    private Context m_context;
    private List<TableDish> m_tableDishList;

    public PrintTableAdapter( Context context, List<TableDish> tableDishList ) {
        m_context = context;
        m_tableDishList = tableDishList;
    }

    private int computePageCount(PrintAttributes printAttributes) {
        int itemsPerPage = 4; // default item count for portrait mode

        PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
        if (!pageSize.isPortrait()) {
            // Six items per page in landscape orientation
            itemsPerPage = 6;
        }

        // Determine number of print items
        //int printItemCount = getPrintItemCount();
        int printItemCount = itemsPerPage;

        return (int) Math.ceil(printItemCount / itemsPerPage);
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        // Create a new PdfDocument with the requested page attributes
        m_pdfDocument = new PrintedPdfDocument( m_context, newAttributes );

        // Respond to cancellation request
        if (cancellationSignal.isCanceled() ) {
            callback.onLayoutCancelled();
            return;
        }

        // Compute the expected number of printed pages
        int pages = computePageCount(newAttributes);

        if (pages > 0) {
            // Return print information to print framework
            PrintDocumentInfo info = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(pages)
                    .build();
            // Content layout reflow is complete
            callback.onLayoutFinished(info, true);
        } else {
            // Otherwise report an error to the print framework
            callback.onLayoutFailed("Page count calculation failed.");
        }
    }

    private String computeLine( int maxLine, String str, boolean right ) {
        if( right ) {
            do {
                str = " " + str;
            } while ( str.length() < maxLine );
        } else {
            do {
                str += " ";
            } while ( str.length() < maxLine );
        }

        return str;
    }

    private void drawPage(PdfDocument.Page page) {
        Canvas canvas = page.getCanvas();

        // units are in points (1/72 of an inch)
        int titleBaseLine = 70;
        int leftMargin = 35;
        int lineBase = 40;

        Paint paint = new Paint();

        paint.setColor(Color.BLACK);
        paint.setTextSize(35);

        canvas.drawText( "AR BREIZH BOUGNATE", leftMargin, titleBaseLine, paint );
        titleBaseLine += lineBase;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        canvas.drawText("Date: " + sdf.format( new Date() ), leftMargin, titleBaseLine, paint);
        titleBaseLine += lineBase * 2;

        leftMargin -= 30;
        lineBase = 35;
        paint.setTextSize(30);

        int designationMaxWith = 7;
        int priceMaxWidth = 2;
        int qtyMaxWidth = 8;
        int totalMawWidth = 5;
        for( TableDish tableDish : m_tableDishList ) {
            if( tableDish.getDish().getDesignation().length() > designationMaxWith ) {
                designationMaxWith = tableDish.getDish().getDesignation().length();
            }

            if( tableDish.getDish().getPrice().toString().length() > priceMaxWidth ) {
                priceMaxWidth = tableDish.getDish().getPrice().toString().length();
            }

            if( String.valueOf( tableDish.getQty() ).length() > qtyMaxWidth ) {
                qtyMaxWidth = String.valueOf( tableDish.getQty() ).length();
            }

            Double total = tableDish.getDish().getPrice() * tableDish.getQty();
            if( String.valueOf( total ).length() > totalMawWidth ) {
                totalMawWidth = String.valueOf( total ).length();
            }
        }

        StringBuilder strb = new StringBuilder();
        strb.append( computeLine( designationMaxWith, "Article", false ) );
        strb.append( computeLine( priceMaxWidth, "PU", false ) );
        strb.append( computeLine( qtyMaxWidth, "Quantité", true ) );
        strb.append( computeLine( totalMawWidth, "Total", true ) );

        canvas.drawText(strb.toString().toUpperCase(), leftMargin, titleBaseLine, paint);
        titleBaseLine += lineBase;

        PdfPTable table = new PdfPTable( new float[] { 2, 1, 2 } );
        canvas.drawText(table.toString().toUpperCase(), leftMargin, titleBaseLine, paint);

        /*for( int i = 0, y = titleBaseLine; i <  m_tableDishList.size(); i++, y += lineBase ) {

            Formatter fmt = new Formatter();
            fmt.format(
                    "%1$" + designationMaxWith + "s%2$" + priceMaxWidth + "s%3$" + qtyMaxWidth + "s%4$" + totalMawWidth + "s",
                    m_tableDishList.get( i ).getDish().getDesignation(),
                    m_tableDishList.get( i ).getDish().getPrice().toString(),
                    String.valueOf( m_tableDishList.get( i ).getQty() ),
                    String.valueOf( m_tableDishList.get( i ).getDish().getPrice() * m_tableDishList.get( i ).getQty() )
                    );
            /*strb = new StringBuilder();
            strb.append( computeLine( designationMaxWith, m_tableDishList.get( i ).getDish().getDesignation(),false ) );
            strb.append( computeLine( priceMaxWidth, m_tableDishList.get( i ).getDish().getPrice().toString(),false ) );
            strb.append( computeLine( qtyMaxWidth, String.valueOf( m_tableDishList.get( i ).getQty() ),true ) );
            strb.append( computeLine( totalMawWidth, String.valueOf( m_tableDishList.get( i ).getDish().getPrice() * m_tableDishList.get( i ).getQty() ),true ) );

            canvas.drawText( fmt.toString().toUpperCase(), leftMargin, y, paint);
        }*/
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        // Iterate over each page of the document,
        // check if it's in the output range.
        for (int i = 0; i < pages.length; i++) {
            // Check to see if this page is in the output range.
            /*if (containsPage(pageRanges, i)) {
                // If so, add it to writtenPagesArray. writtenPagesArray.size()
                // is used to compute the next output page index.
                writtenPagesArray.append(writtenPagesArray.size(), i);*/
                PdfDocument.Page page = m_pdfDocument.startPage(i);

                // check for cancellation
                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    m_pdfDocument.close();
                    m_pdfDocument = null;
                    return;
                }

                // Draw page content for printing
                drawPage(page);

                // Rendering is complete, so page can be finalized.
                m_pdfDocument.finishPage(page);
            //}
        }

        // Write PDF document to file
        try {
            m_pdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            m_pdfDocument.close();
            m_pdfDocument = null;
        }
        //PageRange[] writtenPages = computeWrittenPages();
        // Signal the print framework the document is complete
        callback.onWriteFinished( pages );
    }
}
