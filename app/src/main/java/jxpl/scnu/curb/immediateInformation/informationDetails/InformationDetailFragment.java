package jxpl.scnu.curb.immediateInformation.informationDetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.immediateInformation.ImmediateInformation;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;

import static com.google.common.base.Preconditions.checkNotNull;


public class InformationDetailFragment extends Fragment implements InformationDetailContract.View {


    private static final String ARGUMUENT_INFO_ID = "INFO_ID";
    @BindView(R.id.informationKind)
    ImageView informationKind;
    @BindView(R.id.informationTitle)
    TextView informationTitle;
    @BindView(R.id.info_detail_type)
    TextView infoDetailType;
    @BindView(R.id.info_detail_time)
    TextView infoDetailTime;
    @BindView(R.id.info_detail_content)
    TextView infoDetailContent;
    @BindView(R.id.info_detail_content_url)
    TextView infoDetailContentUrl;
    Unbinder unbinder;
    private InformationDetailContract.Presenter mPresenter;


    public InformationDetailFragment() {
        // Required empty public constructor
    }


    public static InformationDetailFragment newInstance(@NonNull int id) {
        Bundle argument = new Bundle();
        argument.putInt(ARGUMUENT_INFO_ID, id);
        InformationDetailFragment informationDetailFragment = new InformationDetailFragment();
        informationDetailFragment.setArguments(argument);
        return informationDetailFragment;
    }

    @Override
    public void setPresenter(@NonNull InformationDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_information_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void showMissingInfo() {
        if (getView() == null) {
            Log.d("InfoDetailView", "showMissingInfo: MissingView");
        } else {
            informationTitle.setText(getString(R.string.info_detail_missed));
            infoDetailContent.setText(getString(R.string.info_loading_error));
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (getView() == null)
            return;
        if (active) {
            informationTitle.setText("");
            infoDetailContent.setText(getString(R.string.info_detail_loading));
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showInfo(@NonNull ImmediateInformation immediateInformation) {

        informationTitle.setText(immediateInformation.getTitle());
        infoDetailType.setText(immediateInformation.getType());

        final Fragment lc_fragment = this;
        /*infoDetailContent.setText(immediateInformation.getContent());
*/
        infoDetailContent.setMovementMethod(LinkMovementMethod.getInstance());

        HtmlText.from(immediateInformation.getContent())
                .setImageLoader(new HtmlImageLoader() {
                    @Override
                    public void loadImage(String para_s, final Callback para_callback) {
                        Glide.with(lc_fragment)
                                .asBitmap()
                                .load(para_s)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        para_callback.onLoadComplete(resource);
                                    }
                                });
                    }

                    @Override
                    public Drawable getDefaultDrawable() {
                        return ContextCompat.getDrawable(checkNotNull(getContext())
                                , R.drawable.ic_info_detail_loading_circle);
                    }

                    @Override
                    public Drawable getErrorDrawable() {
                        return ContextCompat.getDrawable(checkNotNull(getContext())
                                , R.drawable.ic_info_image_error);
                    }

                    @Override
                    public int getMaxWidth() {
                        return infoDetailContent.getWidth();
                    }

                    @Override
                    public boolean fitWidth() {
                        return false;
                    }
                })
                .into(infoDetailContent);

        infoDetailContentUrl.setText(immediateInformation.getContent_url());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
