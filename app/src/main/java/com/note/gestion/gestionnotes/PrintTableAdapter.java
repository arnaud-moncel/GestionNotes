package com.note.gestion.gestionnotes;

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

import com.note.gestion.carte.Dish;
import com.note.gestion.table.TableDish;
import com.note.gestion.vat.Vat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class PrintTableAdapter extends PrintDocumentAdapter {
    private PrintedPdfDocument m_pdfDocument;
    private Context m_context;
    private List<TableDish> m_tableDishList;

    public PrintTableAdapter( Context context, List<TableDish> tableDishList ) {
        m_context = context;
        m_tableDishList = tableDishList;
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
        int pages = 1;
        if( m_tableDishList.size() - 4 > 0 ) {
            pages++;
            pages += ( m_tableDishList.size() - 4 ) / 20;
        }

        // Return print information to print framework
        PrintDocumentInfo info = new PrintDocumentInfo
                .Builder("print_output.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(pages)
                .build();
        // Content layout reflow is complete
        callback.onLayoutFinished(info, true);
    }

    private void drawPages() {
        int nbPage = 0;
        PdfDocument.Page page = m_pdfDocument.startPage( nbPage );
        Canvas canvas = page.getCanvas();

        // units are in points (1/72 of an inch)
        int topMargin = 50;
        int bottomMargin = 750;
        int lineOffset = topMargin;
        int leftMargin = 35;
        int lineBase = 40;

        Paint paint = new Paint();

        paint.setColor(Color.BLACK);
        paint.setTextSize(35);

        canvas.drawText( "AR BREIZH BOUGNATE", leftMargin, lineOffset, paint );
        lineOffset += lineBase;

        canvas.drawText( "SNC LANGEAC PRESSE", leftMargin, lineOffset, paint );
        lineOffset += lineBase;

        canvas.drawText( "35 RUE DU PONT", leftMargin, lineOffset, paint );
        lineOffset += lineBase;

        canvas.drawText( "43300 LANGEAC", leftMargin, lineOffset, paint );
        lineOffset += lineBase;

        canvas.drawText( "Tel: 04 71 77 09 04", leftMargin, lineOffset, paint );
        lineOffset += lineBase;

        canvas.drawText( "Siret : 44379344300039", leftMargin, lineOffset, paint );
        lineOffset += lineBase * 2;

        paint.setTextSize(30);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        canvas.drawText("Date: " + sdf.format( new Date() ), leftMargin, lineOffset, paint);
        lineOffset += lineBase * 2;

        leftMargin -= 30;
        lineBase = 35;

        int designationMaxWith = 21;
        int priceMaxWidth = 6;
        int qtyMaxWidth = 3;
        Map<Vat, Double> invoiceTotalMap = new HashMap<>();

        int margin = leftMargin;
        canvas.drawText( "ARTICLE", margin, lineOffset, paint);

        margin += designationMaxWith * 15;
        canvas.drawText( "PU", margin, lineOffset, paint);

        margin += priceMaxWidth * 15;
        canvas.drawText( "X", margin, lineOffset, paint);

        margin += qtyMaxWidth * 15;
        canvas.drawText( "TOTAL", margin, lineOffset, paint);
        lineOffset += lineBase;

        for( int i = 0; i <  m_tableDishList.size(); i++, lineOffset += lineBase ) {
            if( lineOffset >= bottomMargin ) {
                m_pdfDocument.finishPage( page );
                page = m_pdfDocument.startPage( ++nbPage );
                canvas = page.getCanvas();
                lineOffset = topMargin;
            }

            Dish dish = m_tableDishList.get( i ).getDish();

            margin = leftMargin;
            String str = dish.getDesignation();
            if( str.length() > designationMaxWith - 1 ) {
                str = str.substring( 0, designationMaxWith );
            }
            canvas.drawText( str, margin, lineOffset, paint);

            margin += designationMaxWith * 15;
            str = String.format( "%.2f", dish.getPrice() );
            if ( str.length() < priceMaxWidth - 1 ) {
                str = String.format( "%6.2f", dish.getPrice() );
            }
            canvas.drawText( str, margin, lineOffset, paint);

            margin += priceMaxWidth * 15;
            canvas.drawText( String.valueOf( m_tableDishList.get( i ).getQty() ), margin, lineOffset, paint);

            margin += qtyMaxWidth * 15;
            Double total = dish.getPrice() * m_tableDishList.get( i ).getQty();
            Vat vat = dish.getVat();
            if( !invoiceTotalMap.containsKey( vat ) ) {
                invoiceTotalMap.put( vat, total );
            } else {
                invoiceTotalMap.put( vat, invoiceTotalMap.get( vat ) + total );
            }
            str = String.format( "%.2f", total );
            if ( str.length() < priceMaxWidth - 1 ) {
                str = String.format( "%6.2f", total );
            }
            canvas.drawText( str, margin, lineOffset, paint);
        }
        lineOffset += lineBase;
        if( lineOffset >= bottomMargin ) {
            m_pdfDocument.finishPage( page );
            page = m_pdfDocument.startPage( ++nbPage );
            canvas = page.getCanvas();
            lineOffset = topMargin;
        }

        Double total = 0.0;
        for( Map.Entry<Vat, Double> entry : invoiceTotalMap.entrySet() ) {
            if( lineOffset >= bottomMargin ) {
                m_pdfDocument.finishPage( page );
                page = m_pdfDocument.startPage( ++nbPage );
                canvas = page.getCanvas();
                lineOffset = topMargin;
            }
            Double vat = ( entry.getValue() / ( 1 + entry.getKey().getPercent() / 100 ) ) * ( entry.getKey().getPercent() / 100 );

            String str = entry.getKey().getDesignation() + "  :  " + String.format( "%.2f", vat );
            canvas.drawText( str, leftMargin, lineOffset, paint);

            total += entry.getValue();

            lineOffset += lineBase;
        }
        lineOffset += lineBase;
        if( lineOffset >= bottomMargin ) {
            m_pdfDocument.finishPage( page );
            page = m_pdfDocument.startPage( ++nbPage );
            canvas = page.getCanvas();
            lineOffset = topMargin;
        }

        canvas.drawText( "Total  :  " + String.format( "%.2f", total ), leftMargin, lineOffset, paint);
        m_pdfDocument.finishPage( page );
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {

        drawPages();

        if (cancellationSignal.isCanceled()) {
            callback.onWriteCancelled();
            m_pdfDocument.close();
            m_pdfDocument = null;
            return;
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
