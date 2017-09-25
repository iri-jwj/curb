package jxpl.scnu.curb.immediateInformation;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.homePage.homePageContract;

import static android.support.v4.util.Preconditions.checkNotNull;



public class informationFragment extends Fragment implements informationContract.View {

    private informationContract.Presenter presenter;
    private infoAdapter minfoAdapter;
    private List<immediateInformation> immediateInformations;

    public informationFragment() {
        // Required empty public constructor
    }

    public static informationFragment newInstance() {

        return new informationFragment();
    }

    @Override
    public void setPresenter(@NonNull informationContract.Presenter mPresenter){
        presenter=checkNotNull(mPresenter);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        minfoAdapter=new infoAdapter(immediateInformations);
    }

    @Override
    public int getImageIdByType(String type){
        int imageId;
        switch (type){
            case "EDU":
                imageId= R.drawable.item_edu_info;
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
        RecyclerView recyclerView=info.findViewById(R.id.info_recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(info.getContext());//不明代码= =
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(minfoAdapter);

        //SET UP floatingActionBar
        FloatingActionButton fab = getActivity().findViewById(R.id.info_floatingAb);
        fab.setImageResource(R.drawable.fliter_arrow);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return info;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    class infoAdapter extends RecyclerView.Adapter<infoAdapter.ViewHolder>{
        private List<immediateInformation> immediateInformations;

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView infoImage;
            TextView infoTitle;
            TextView infoTime;

             ViewHolder(View itemView) {
                super(itemView);
                infoImage=itemView.findViewById(R.id.info_item_image);
                infoTitle=itemView.findViewById(R.id.info_item_title);
                infoTime=itemView.findViewById(R.id.info_item_time);
            }
        }

        public infoAdapter(List<immediateInformation> immediateInformations) {
            this.immediateInformations=immediateInformations;
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

    @Override
    public void showFilteringPopUpMenu(){
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.info_floatingAb));
        popup.getMenuInflater().inflate(R.menu.info_filter, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.info_education:
                        presenter.setFiltering(informationFilter.EDU_INFORMATIONS);
                        break;
                    case R.id.info_scholar:
                        presenter.setFiltering(informationFilter.SCHOLAR_INFORMATIONS);
                        break;
                    case R.id.info_work_study:
                        presenter.setFiltering(informationFilter.WORK_STUDY_INFORMATIONS);
                        break;
                    case R.id.info_practice:
                        presenter.setFiltering(informationFilter.PRA_INFORMATIONS);
                        break;
                    default:
                        presenter.setFiltering(informationFilter.ALL_INFORMATIONS);
                        break;
                }
               presenter.loadTasks(false);
                return true;
            }
        });

        popup.show();
    }

}
