package com.pratham.pradigikids.ui.pdf_viewer;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;

import com.pratham.pradigikids.BaseActivity;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.BlurPopupDialog.BlurPopupWindow;
import com.pratham.pradigikids.custom.pdf.BookFlipPageTransformer;
import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.models.Modal_Score;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import static com.pratham.pradigikids.PrathamApplication.scoreDao;

@EActivity(R.layout.activity_pdf_viewer)
public class Activity_PdfViewer extends BaseActivity implements PDFContract.pdf_View {

    private static MediaPlayer page_flip_mp;
    private static ArrayList<Bitmap> bitmaps;

    @Bean(PDF_PresenterImpl.class)
    PDFContract.pdfPresenter pdf_presenter;
    @ViewById(R.id.pdf_curl_view)
    ViewPager pdf_curl_view;
    private String startTime;
    private String resId;
    private BlurPopupWindow nextDialog;

    @AfterViews
    public void initialize() {
        pdf_presenter.setView(Activity_PdfViewer.this);
        page_flip_mp = MediaPlayer.create(this, R.raw.page_flip);
        startTime = PD_Utility.getCurrentDateTime();
        resId = getIntent().getStringExtra("resId");
        pdf_presenter.generateImageFromPdf(getIntent().getStringExtra("pdfPath"));
    }

    @Override
    public void onBackPressed() {
        boolean backpressedFlag = true;
        addScoreToDB();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Background
    public void addScoreToDB() {
        Modal_Score modalScore = new Modal_Score();
        modalScore.setSessionID(FastSave.getInstance().getString(PD_Constant.SESSIONID, ""));
        if (PrathamApplication.isTablet)
            modalScore.setGroupID(FastSave.getInstance().getString(PD_Constant.GROUPID, "no_group"));
        else
            modalScore.setStudentID(FastSave.getInstance().getString(PD_Constant.STUDENTID, "no_student"));
        modalScore.setDeviceID(PD_Utility.getDeviceID());
        modalScore.setResourceID(resId);
        modalScore.setQuestionId(0);
        modalScore.setScoredMarks(0);
        modalScore.setTotalMarks((bitmaps.size() > 0) ? bitmaps.size() : 0);
        modalScore.setStartDateTime(startTime);
        modalScore.setEndDateTime(PD_Utility.getCurrentDateTime());
        modalScore.setLevel(0);
        modalScore.setLabel("_");
        modalScore.setSentFlag(0);
        scoreDao.insert(modalScore);
    }

    @Click(R.id.close_pdf)
    public void setClose_pdf() {
        onBackPressed();
    }

    @UiThread
    @Override
    public void recievedBitmaps(ArrayList<Bitmap> bits) {
        bitmaps = bits;
        PDFPagerAdapter pagerAdapter = new PDFPagerAdapter(Activity_PdfViewer.this, bitmaps);
        pdf_curl_view.setAdapter(pagerAdapter);
        pdf_curl_view.setClipToPadding(false);
        BookFlipPageTransformer transformer = new BookFlipPageTransformer();
        transformer.setEnableScale(true);
        pdf_curl_view.setPageTransformer(true, transformer);
        pdf_curl_view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                page_flip_mp.start();
            }

            @Override
            public void onPageSelected(int i) {
                Log.d("onPageSelected:::", "" + i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @UiThread
    public void showNextDialog() {
        nextDialog = new BlurPopupWindow.Builder(Activity_PdfViewer.this)
                .setContentView(R.layout.dialog_next_content)
                .setGravity(Gravity.CENTER)
                .setScaleRatio(0.2f)
                .bindClickListener(v -> {
                    nextDialog.dismiss();
                    finish();
                }, R.id.txt_close)
                .setScaleRatio(0.2f)
                .setBlurRadius(8)
                .setTintColor(0x30000000)
                .build();
        nextDialog.show();
    }
}
