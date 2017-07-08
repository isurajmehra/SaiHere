package com.sairajen.saihere.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sairajen.saihere.R;
import com.sairajen.saihere.fragments.AnswerYourQuestion;
import com.sairajen.saihere.fragments.Blessings;
import com.sairajen.saihere.fragments.DailyMessage;
import com.sairajen.saihere.fragments.Gallery;
import com.sairajen.saihere.fragments.GroupChant;
import com.sairajen.saihere.fragments.MenuHome;
import com.sairajen.saihere.fragments.Miracles;
import com.sairajen.saihere.fragments.OnlinePrayers;
import com.sairajen.saihere.fragments.Others;
import com.sairajen.saihere.fragments.Stories;
import com.sairajen.saihere.fragments.Videos;
import com.sairajen.saihere.fragments.Wallpapers;
import com.sairajen.saihere.helper.Funcs;
import com.sairajen.saihere.model.HomeMenu;
import java.util.List;

/**
 * @author Gmonetix
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder>{

    private Context context;
    private List<HomeMenu> menuList;

    public MenuAdapter(Context context, List<HomeMenu> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_menu,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.menuThumbnail.setImageDrawable(context.getResources().getDrawable(menuList.get(position).getThumbnail()));
        holder.menuTitle.setText(menuList.get(position).getTitle());
        holder.menuSubTitle.setText(menuList.get(position).getSubTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 12) {
                    Funcs.openLink(context,"http://www.saihere.com/more-app.html");
                } else if (position == 13) {
                    Funcs.shareApp(context);
                } else if (position == 6) {
                    Funcs.openLink(context,"http://www.saihere.com/shri-sai-satcharitra-languages.html");
                } else {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,getItem(position)).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    private Fragment getItem(int pos) {
        if (pos == 0)
            return new DailyMessage();
        if (pos == 1)
            return new OnlinePrayers();
        if (pos == 2)
            return new AnswerYourQuestion();
        if (pos == 3)
            return new Gallery();
        if (pos == 4)
            return new Blessings();
        if (pos == 5)
            return new Stories();
        if (pos == 6)
            return null;
        if (pos == 7)
            return new Miracles();
        if (pos == 8)
            return new GroupChant();
        if (pos == 9)
            return new Videos();
        if (pos == 10)
            return new Wallpapers();
        if (pos == 11)
            return new Others();
        if (pos == 12)
            return null;
        if (pos == 13)
            return null;
        return new MenuHome();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        protected ImageView menuThumbnail;
        protected TextView menuTitle, menuSubTitle;

        public MyViewHolder(View view) {
            super(view);
            menuThumbnail = (ImageView) view.findViewById(R.id.homeMenuImage);
            menuTitle = (TextView) view.findViewById(R.id.homeMenuTitle);
            menuSubTitle = (TextView) view.findViewById(R.id.homeMenuSubTitle);
        }
    }

}
