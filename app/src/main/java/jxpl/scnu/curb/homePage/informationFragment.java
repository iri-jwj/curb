package jxpl.scnu.curb.homePage;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.immediateInformation.immediateInformation;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link informationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link informationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class informationFragment extends Fragment implements homePageContract.View {

    private homePageContract.Presenter presenter;
    public informationFragment() {
        // Required empty public constructor
    }

    public static informationFragment newInstance() {

        return new informationFragment();
    }

    @Override
    public void setPresenter(@NonNull homePageContract.Presenter mPresenter){
        presenter=checkNotNull(mPresenter);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getImageIdByType(String type){
        int imageId;
        switch (type){
            case "EDU":
                imageId= R.drawable.item_eduInfo;
                break;
            case "PRA":
                imageId=R.drawable.pra;
                break;
            case "SCHOLAR":
                imageId=R.drawable.scholar;
                break;
            default:
                imageId=0;
        }
        return imageId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View info=inflater.inflate(R.layout.fragment_information, container, false);
        RecyclerView recyclerView=(RecyclerView) info.findViewById(R.id.info_recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(info.getContext());//不明代码= =
        recyclerView.setLayoutManager(layoutManager);
        return info;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public  class infoAdapter extends RecyclerView.Adapter<infoAdapter.ViewHolder>{
        private List<immediateInformation> immediateInformations;

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView infoImage;
            TextView infoTitle;
            TextView infoTime;

            public ViewHolder(View itemView) {
                super(itemView);
                infoImage=itemView.findViewById(R.id.info_item_image);
                infoTitle=itemView.findViewById(R.id.info_item_title);
                infoTime=itemView.findViewById(R.id.info_item_time);
            }
        }

        public infoAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType){
            View view=LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.informations_item,viewGroup,false);
            return  new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            immediateInformation immediateInformation=immediateInformations.get(position);
            holder.infoImage.setImageResource(getImageIdByType(immediateInformation.getType()));
            holder.infoTitle.setText(immediateInformation.getTitle());
            holder.infoTime.setText(immediateInformation.getTime());
        }

        @Override
        public int getItemCount(){
            return immediateInformations.size();
        }
    }


}
