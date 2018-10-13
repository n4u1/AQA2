package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.util.ListViewSearchCategoryAdapter;
import com.n4u1.AQA.AQA.util.ListViewSearchTitleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class SearchHomeActivity extends AppCompatActivity {

    private ListView listViewCategory;
    private ListView listViewTitle;
    private ArrayList<String> stringArrayCategoryList;
    private ArrayList<String> stringArrayTitleList;


    private ListViewSearchCategoryAdapter adapterCategory;
    private ListViewSearchTitleAdapter adapterTitle;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFireBaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_home);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_aqa_custom);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("");
        }

        listViewCategory = findViewById(R.id.searchHomeActivity_listView_category);
        listViewTitle = findViewById(R.id.searchHomeActivity_listView_title);
        mFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();



        //https://stackoverflow.com/questions/31966453/firebase-where-to-call-addchildeventlistener-so-that-i-can-save-bandwidth
        //위의 주소에서처럼 많은량의 user_contents가 누적되었을경우 SharedPreferences(?) 를 이용해서 이어서 검색하게함
        //Query newItems = mDatabaseReference.orderByKey().startAt(lastChildKey);
        //검색 대상인 title을 arraylist로 뽑아서 리스트업
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user_contents");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> tmp = new ArrayList<>();

                Iterator<DataSnapshot> contentDTOIterator = dataSnapshot.getChildren().iterator();

                while (contentDTOIterator.hasNext()) {
                    final ContentDTO contentDTO = contentDTOIterator.next().getValue(ContentDTO.class);
                    tmp.add(contentDTO.title);
                }
                Collections.reverse(tmp);
                setTitleData(tmp);
                setCategoryData();

                adapterCategory = new ListViewSearchCategoryAdapter(SearchHomeActivity.this, R.layout.search_item_listview, stringArrayCategoryList);
                adapterTitle = new ListViewSearchTitleAdapter(SearchHomeActivity.this, R.layout.search_item_title_listview, stringArrayTitleList);
                listViewCategory.setAdapter(adapterCategory);
                listViewTitle.setAdapter(adapterTitle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchHomeActivity.this, (String)parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
//                mFirebaseDatabase.getReference().child("users").child(mFireBaseUser.getUid()).child("search_flag_category").setValue(parent.getItemAtPosition(position));
                Intent intent = new Intent(SearchHomeActivity.this, SearchResultActivity.class);
                intent.putExtra("searchCategory", (String)parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });


        listViewTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchHomeActivity.this, (String)parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
//                mFirebaseDatabase.getReference().child("users").child(mFireBaseUser.getUid()).child("search_flag_title").setValue(parent.getItemAtPosition(position));
                Intent intent = new Intent(SearchHomeActivity.this, SearchResultActivity.class);
                intent.putExtra("searchTitle", (String)parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });





    }


    //게시글 제목 검색결과 list
    private void setTitleData(ArrayList<String> tmpString) {
        stringArrayTitleList = new ArrayList<>();
        stringArrayTitleList.addAll(tmpString);
    }


    //카테고리 검색결과 list
    private void setCategoryData() {
        stringArrayCategoryList = new ArrayList<>();
        stringArrayCategoryList.add("개그");
        stringArrayCategoryList.add("건강");
        stringArrayCategoryList.add("게임");
        stringArrayCategoryList.add("결혼");
        stringArrayCategoryList.add("경제");
        stringArrayCategoryList.add("고민");
        stringArrayCategoryList.add("공포");
        stringArrayCategoryList.add("과학");
        stringArrayCategoryList.add("군대");
        stringArrayCategoryList.add("꿈");
        stringArrayCategoryList.add("낚시");
        stringArrayCategoryList.add("노래");
        stringArrayCategoryList.add("농구");
        stringArrayCategoryList.add("다이어트");
        stringArrayCategoryList.add("동물");
        stringArrayCategoryList.add("드라마");
        stringArrayCategoryList.add("똥");
        stringArrayCategoryList.add("롤");
        stringArrayCategoryList.add("만화");
        stringArrayCategoryList.add("맛집");
        stringArrayCategoryList.add("모바일게임");
        stringArrayCategoryList.add("바둑");
        stringArrayCategoryList.add("배그");
        stringArrayCategoryList.add("법");
        stringArrayCategoryList.add("뷰티");
        stringArrayCategoryList.add("사건사고");
        stringArrayCategoryList.add("사진");
        stringArrayCategoryList.add("사회");
        stringArrayCategoryList.add("세월호");
        stringArrayCategoryList.add("소개팅");
        stringArrayCategoryList.add("술");
        stringArrayCategoryList.add("스포츠");
        stringArrayCategoryList.add("시사");
        stringArrayCategoryList.add("식물");
        stringArrayCategoryList.add("심리");
        stringArrayCategoryList.add("아무거나");
        stringArrayCategoryList.add("아이폰");
        stringArrayCategoryList.add("아침");
        stringArrayCategoryList.add("안드로이드");
        stringArrayCategoryList.add("안주");
        stringArrayCategoryList.add("애니메이션");
        stringArrayCategoryList.add("야구");
        stringArrayCategoryList.add("여행");
        stringArrayCategoryList.add("역사");
        stringArrayCategoryList.add("연애");
        stringArrayCategoryList.add("연예인");
        stringArrayCategoryList.add("영화");
        stringArrayCategoryList.add("와우");
        stringArrayCategoryList.add("요리");
        stringArrayCategoryList.add("웹툰");
        stringArrayCategoryList.add("유머");
        stringArrayCategoryList.add("육아");
        stringArrayCategoryList.add("음악");
        stringArrayCategoryList.add("의료");
        stringArrayCategoryList.add("인테리어");
        stringArrayCategoryList.add("자동차");
        stringArrayCategoryList.add("자전거");
        stringArrayCategoryList.add("저녁");
        stringArrayCategoryList.add("점심");
        stringArrayCategoryList.add("정치");
        stringArrayCategoryList.add("종교");
        stringArrayCategoryList.add("지식");
        stringArrayCategoryList.add("직업");
        stringArrayCategoryList.add("책");
        stringArrayCategoryList.add("축구");
        stringArrayCategoryList.add("취업");
        stringArrayCategoryList.add("컴퓨터");
        stringArrayCategoryList.add("패션");
        stringArrayCategoryList.add("휴가");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchView searchView;
        getMenuInflater().inflate(R.menu.search_home_menu, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setFocusable(true);  //자동포커스
        searchView.setIconified(false); //자동포커스
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapterCategory.filter("");
                    adapterTitle.filter("");
                    listViewCategory.clearTextFilter();
                    listViewTitle.clearTextFilter();
                } else {
                    adapterTitle.filter(newText);
                    adapterCategory.filter(newText);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_home:
                Intent intent = new Intent(SearchHomeActivity.this, HomeActivity.class);
//                overridePendingTransition(0, 0);
                startActivity(intent);
//                overridePendingTransition(0, 0);
                break;

            case android.R.id.home:
                Intent intentAqa = new Intent(SearchHomeActivity.this, HomeActivity.class);
                intentAqa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentAqa);
        }
        return super.onOptionsItemSelected(item);
    }
}
