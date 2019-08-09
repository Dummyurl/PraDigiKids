package com.pratham.pradigikids.ui.pdf_viewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@EBean
public class PDF_PresenterImpl implements PDFContract.pdfPresenter {
    private final Context context;
    private PDFContract.pdf_View pdf_view;

    public PDF_PresenterImpl(Context context) {
        this.context = context;
    }

    @Override
    public void setView(Activity_PdfViewer activity_pdfViewer) {
        pdf_view = activity_pdfViewer;
    }

    @Background
    @Override
    public void generateImageFromPdf(String resPath) {
        ArrayList<Bitmap> pdf = new ArrayList<>();
        try {
            PdfRenderer renderer = new PdfRenderer(getSeekableFileDescriptor(resPath));
            // let us just render all pages
            final int pageCount = renderer.getPageCount();
            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = renderer.openPage(i);
                // say we render for showing on the screen
                Bitmap mBitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                // do stuff with the bitmap
                pdf.add(mBitmap);
                // close the page
                page.close();
            }
            renderer.close();
            pdf_view.recievedBitmaps(pdf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ParcelFileDescriptor getSeekableFileDescriptor(String resPath) {
        ParcelFileDescriptor fd = null;
        try {
            fd = context.getContentResolver().openFileDescriptor(Uri.fromFile(new File(resPath)), "r");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fd;
    }
}
