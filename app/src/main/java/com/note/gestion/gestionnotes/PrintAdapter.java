package com.note.gestion.gestionnotes;

import android.content.Context;
import android.os.AsyncTask;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.note.gestion.carte.Dish;
import com.note.gestion.table.TableDish;
import com.note.gestion.vat.Vat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintAdapter implements ReceiveListener {

    private Context m_context;
    private Printer m_printer;

    private boolean initializeObject() {
        try {
            m_printer = new Printer( Printer.TM_T20, Printer.MODEL_ANK, m_context );
        }
        catch (Exception e) {
            return false;
        }

        m_printer.setReceiveEventListener(this);

        return true;
    }

    private boolean createReceiptData( int tableId, List<TableDish> tableDishList ) {
        StringBuilder textData = new StringBuilder();

        if ( m_printer == null ) {
            return false;
        }

        try {
            m_printer.addTextAlign(Printer.ALIGN_CENTER);

            textData.append("AR BREIZH BOUGNATE\n");
            m_printer.addTextSize(2, 2);
            m_printer.addText( textData.toString() );
            textData.delete(0, textData.length());

            textData.append("SNC LANGEAC PRESSE\n");
            textData.append("35 RUE DU PONT\n");
            textData.append("43300 LANGEAC\n");
            textData.append("Tel: 04 71 77 09 04\n");
            textData.append( "Siret : 44379344300039\n" );
            m_printer.addTextSize(1, 1);
            m_printer.addText(textData.toString());
            textData.delete(0, textData.length());

            m_printer.addFeedLine( 1 );
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            textData.append( "Date: " ).append( sdf.format( new Date() ) ).append( "\n" );
            textData.append( "N° : " ).append( tableId ).append( "\n" );
            textData.append("--------------------------------------\n");
            m_printer.addText(textData.toString());
            textData.delete(0, textData.length());

            Map<Vat, Double> invoiceTotalMap = new HashMap<>();

            for( TableDish tableDish : tableDishList ) {
                Dish dish = tableDish.getDish();

                Double price;
                if( dish.getPrice() != null ) {
                    price = dish.getPrice();
                } else {
                    price = tableDish.getPrice();
                }

                String des = dish.getDesignation();
                if( des.length() > 21 ) {
                    des = des.substring( 0, 21 );
                }

                Double total = price * tableDish.getQty();
                textData.append( String.format(
                        "%-21s %6.2f %3d %6.2f",
                        des,
                        price,
                        tableDish.getQty(),
                        total
                ) ).append( "\n" );

                Vat vat = dish.getVat();
                if( !invoiceTotalMap.containsKey( vat ) ) {
                    invoiceTotalMap.put( vat, total );
                } else {
                    invoiceTotalMap.put( vat, invoiceTotalMap.get( vat ) + total );
                }
            }
            m_printer.addText( textData.toString() );
            textData.delete( 0, textData.length() );

            textData.append("--------------------------------------\n");
            Double total = 0.0;
            for( Map.Entry<Vat, Double> entry : invoiceTotalMap.entrySet() ) {
                Double vat = ( entry.getValue() / ( 1 + entry.getKey().getPercent() / 100 ) ) * ( entry.getKey().getPercent() / 100 );

                textData.append( String.format(
                        "%-19s : %6.2f %3s %6.2s",
                        entry.getKey().getDesignation(),
                        vat,
                        "",
                        ""
                ) ).append( "\n" );

                total += entry.getValue();
            }
            m_printer.addText(textData.toString());
            m_printer.addFeedLine(1);
            textData.delete(0, textData.length());

            textData.append( String.format(
                    "%-4s : %3.2f %1s %3.2s",
                    "TOTAL",
                    total,
                    "€",
                    ""
            ) );
            m_printer.addTextSize(2, 2);
            m_printer.addText( textData.toString() );
            m_printer.addFeedLine(3);
            textData.delete(0, textData.length());

            textData.append( "MERCI DE VOTRE VISITE.\n" );
            textData.append( "KENAVO" );
            m_printer.addTextSize(1, 1);
            m_printer.addText( textData.toString() );
            m_printer.addFeedLine(2);

            m_printer.addCut(Printer.CUT_FEED);
        }
        catch (Exception e) {
            return false;
        }

        return true;
    }

    private void finalizeObject() {
        if (m_printer == null) {
            return;
        }

        m_printer.clearCommandBuffer();

        m_printer.setReceiveEventListener(null);

        m_printer = null;
    }

    private boolean connectPrinter() {
        boolean isBeginTransaction = false;

        if (m_printer == null) {
            return false;
        }

        try {
            m_printer.connect("TCP:192.168.1.35", Printer.PARAM_DEFAULT);
        }
        catch (Epos2Exception e) {
            return false;
        }

        try {
            m_printer.beginTransaction();
            isBeginTransaction = true;
        }
        catch (Exception e) {
            isBeginTransaction = false;
        }

        if (!isBeginTransaction) {
            try {
                m_printer.disconnect();
            }
            catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }

        return true;
    }

    private boolean printData() {
        if (m_printer == null) {
            return false;
        }

        if (!connectPrinter()) {
            return false;
        }

        PrinterStatusInfo status = m_printer.getStatus();

        if (status.getConnection() == Printer.FALSE || status.getOnline() == Printer.FALSE ) {
            try {
                m_printer.disconnect();
            }
            catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        try {
            m_printer.sendData(Printer.PARAM_DEFAULT);
        }
        catch (Exception e) {
            try {
                m_printer.disconnect();
            }
            catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }

    public boolean doPrint( Context context, int tableId, List<TableDish> tableDishList ) {
        m_context = context;
        if (!initializeObject()) {
            return false;
        }

        if (!createReceiptData( tableId, tableDishList )) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    @Override
    public void onPtrReceive(Printer printer, int i, PrinterStatusInfo printerStatusInfo, String s) {

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids){
                try {
                    m_printer.clearCommandBuffer();
                    m_printer.setReceiveEventListener( null );
                    m_printer.endTransaction();
                    m_printer.disconnect();
                }
                catch( Epos2Exception e ) {

                }
                m_printer = null;
                return null;
            }
        }.execute();
    }
}
