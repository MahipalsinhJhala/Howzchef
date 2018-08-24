package com.houz.chef.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.activity.ActivityCart;
import com.houz.chef.activity.DrawerActivity;
import com.houz.chef.adapter.HomeAdapter;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.HomeFragmentBinding;
import com.houz.chef.interfaces.OnItemClickListener;
import com.houz.chef.interfaces.OnLoadMoreListener;
import com.houz.chef.modelBean.Data;
import com.houz.chef.modelBean.Datum;
import com.houz.chef.modelBean.GetCart;
import com.houz.chef.modelBean.GetProductBean;
import com.houz.chef.modelBean.ModelBean;
import com.houz.chef.modelBean.Product;
import com.houz.chef.modelBean.ProductBean;
import com.houz.chef.modelBean.QuantityUpdateResponse;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.Const;
import com.houz.chef.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, OnItemClickListener {

    HomeFragmentBinding binding;
    private int pageProduct = 0;
    private int lastPage = 0;
    private HomeAdapter homeAdapter;

    private ArrayList<ProductBean> cartProductBeansArray = new ArrayList<>();
    private ArrayList<ProductBean> productBeansArray = new ArrayList<>();
    private ArrayList<ProductBean> filterProductBeansArray = new ArrayList<>();
    private boolean isSearchMode = false; // True when user search any food item, False when user clear search field
    private ArrayList<Datum> cartItem = new ArrayList<>();
    String sortType = "";
    String filterCategory = "";
    String filterType = "";

    @Override
    public int getLayoutResId() {
        return R.layout.home_fragment;
    }


    private Point getPointOfView(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Point(location[0], location[1]);
    }

    public void init() {
        pageProduct = 0;
        binding = (HomeFragmentBinding) getBindingObj();
        Preferences preferences = new Preferences(getContext());
        filterCategory = preferences.getFilterCategoryData();
        filterType = preferences.getFilterTypeData();
        sortType = preferences.getSortingData();
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerActivity) context).ToggleDrawer();
            }
        });

        final GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (homeAdapter != null) {
                    switch (homeAdapter.getItemViewType(position)) {
                        case 1:
                            return 2;
                        case 0:
                            return 1; //number of columns of the grid
                        default:
                            return -1;
                    }
                } else {
                    return -1;
                }
            }
        });
        binding.recyclerView.setLayoutManager(layoutManager/*new GridLayoutManager(context, 2)*/);

        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageProduct = 0;
                callgetCart();
            }
        });

        if (homeAdapter == null) {
            homeAdapter = new HomeAdapter(context, binding.recyclerView, this);
            homeAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (pageProduct < lastPage) {
                        homeAdapter.addProgress();
                        callgetCart();
                    } else {
                        homeAdapter.removeProgress();
                        homeAdapter.setLoaded();
                    }
                }
            });
        }
        binding.recyclerView.setAdapter(homeAdapter);
        pageProduct = 0;
        homeAdapter.clearAll();
        /*binding.tvProceed.setOnClickListener(this);*/
        binding.btnFilter.setOnClickListener(this);
        binding.imgCart.setOnClickListener(this);
        binding.edtSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click
                    String search = binding.edtSearchBar.getText().toString().trim();
                    //if (!search.isEmpty()) {
                    isSearchMode = true;
                    pageProduct = 0;
                    callgetCart();
                    /*} else {
                        CommonUtils.toast(getActivity(), "Please enter search keyword.");
                    }*/
                    return true;
                }
                return false;
            }
        });

        binding.edtSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isSearchMode && s.toString().isEmpty()) {
                    isSearchMode = false;
                    pageProduct = 0;
                    callgetAllMenu();
                }
            }
        });
        callgetCart();

        binding.llFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_filter, null);
                builder.setView(dialogView);
                final AlertDialog alert = builder.show();

                /*Point point = getPointOfView(binding.llFilter);
                WindowManager.LayoutParams windowManager = alert.getWindow().getAttributes();

                windowManager.gravity = Gravity.TOP | Gravity.LEFT;
                //wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                //windowManager.height = WindowManager.LayoutParams.MATCH_PARENT;
                windowManager.x = point.x;   //x position
                windowManager.y = point.y;   //y position
                alert.getWindow().setAttributes(windowManager);
*/
                ImageView ivClose = (ImageView) alert.findViewById(R.id.ivClose);
                final AppCompatCheckBox chkIndian = alert.findViewById(R.id.chkIndian);
                final AppCompatCheckBox chkItalina = alert.findViewById(R.id.chkItalian);
                final AppCompatCheckBox chkChinese = alert.findViewById(R.id.chkChinese);
                final AppCompatCheckBox chkMaxican = alert.findViewById(R.id.chkMaxican);
                final AppCompatCheckBox chkPunjabi = alert.findViewById(R.id.chkPunjabi);

                final RadioButton rbVeg = alert.findViewById(R.id.rbVeg);
                final RadioButton rbNonVeg = alert.findViewById(R.id.rbNonVeg);
                final RadioButton rbBoth = alert.findViewById(R.id.rbBoth);

                if (filterType.equalsIgnoreCase("0")) {
                    rbNonVeg.setChecked(true);
                }
                if (filterType.equalsIgnoreCase("1")) {
                    rbVeg.setChecked(true);
                }
                if (filterType.equalsIgnoreCase("2")) {
                    rbBoth.setChecked(true);
                }
                if (filterCategory.contains("indian")) {
                    chkIndian.setChecked(true);
                }
                if (filterCategory.contains("italian")) {
                    chkItalina.setChecked(true);
                }
                if (filterCategory.contains("chinese")) {
                    chkChinese.setChecked(true);
                }
                if (filterCategory.contains("maxican")) {
                    chkMaxican.setChecked(true);
                }
                if (filterCategory.contains("punjabi")) {
                    chkPunjabi.setChecked(true);
                }

                rbNonVeg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            filterType = "0";
                        }
                    }
                });

                rbVeg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            filterType = "1";
                        }
                    }
                });

                rbBoth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            filterType = "2";
                        }
                    }
                });
                chkIndian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            if (filterCategory.length() > 0 && !filterCategory.contains("indian")) {
                                filterCategory = filterCategory + ",indian";
                            } else if (!filterCategory.contains("indian")) {
                                filterCategory = "indian";
                            }
                        } else {
                            if (filterCategory.contains(",indian")) {
                                filterCategory = filterCategory.replace(",indian", "");
                            } else if (filterCategory.contains("indian,")) {
                                filterCategory = filterCategory.replace("indian,", "");
                            } else if (filterCategory.contains("indian")) {
                                filterCategory = filterCategory.replace("indian", "");
                            }
                        }
                    }
                });

                chkItalina.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            if (filterCategory.length() > 0 && !filterCategory.contains("italian")) {
                                filterCategory = filterCategory + ",italian";
                            } else if (!filterCategory.contains("italian")) {
                                filterCategory = "italian";
                            }
                        } else {
                            if (filterCategory.contains(",italian")) {
                                filterCategory = filterCategory.replace(",italian", "");
                            } else if (filterCategory.contains("italian,")) {
                                filterCategory = filterCategory.replace("italian,", "");
                            } else if (filterCategory.contains("italian")) {
                                filterCategory = filterCategory.replace("italian", "");
                            }
                        }
                    }
                });

                chkMaxican.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            if (filterCategory.length() > 0 && !filterCategory.contains("maxican")) {
                                filterCategory = filterCategory + ",maxican";
                            } else if (!filterCategory.contains("maxican")) {
                                filterCategory = "maxican";
                            }
                        } else {
                            if (filterCategory.contains(",maxican")) {
                                filterCategory = filterCategory.replace(",maxican", "");
                            } else if (filterCategory.contains("maxican,")) {
                                filterCategory = filterCategory.replace("maxican,", "");
                            } else if (filterCategory.contains("maxican")) {
                                filterCategory = filterCategory.replace("maxican", "");
                            }
                        }
                    }
                });

                chkChinese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            if (filterCategory.length() > 0 && !filterCategory.contains("chinese")) {
                                filterCategory = filterCategory + ",chinese";
                            } else if (!filterCategory.contains("chinese")) {
                                filterCategory = "chinese";
                            }
                        } else {
                            if (filterCategory.contains(",chinese")) {
                                filterCategory = filterCategory.replace(",chinese", "");
                            } else if (filterCategory.contains("chinese,")) {
                                filterCategory = filterCategory.replace("chinese,", "");
                            } else if (filterCategory.contains("chinese")) {
                                filterCategory = filterCategory.replace("chinese", "");
                            }
                        }
                    }
                });

                chkPunjabi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            if (filterCategory.length() > 0 && !filterCategory.contains("punjabi")) {
                                filterCategory = filterCategory + ",punjabi";
                            } else if (!filterCategory.contains("punjabi")) {
                                filterCategory = "punjabi";
                            }
                        } else {
                            if (filterCategory.contains(",punjabi")) {
                                filterCategory = filterCategory.replace(",punjabi", "");
                            } else if (filterCategory.contains("punjabi,")) {
                                filterCategory = filterCategory.replace("punjabi,", "");
                            } else if (filterCategory.contains("punjabi")) {
                                filterCategory = filterCategory.replace("punjabi", "");
                            }
                        }
                    }
                });
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });

                final TextView tvApply = alert.findViewById(R.id.tvApply);
                final TextView tvReset = alert.findViewById(R.id.tvReset);

                tvApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        pageProduct = 0;
                        Preferences preferences = new Preferences(getActivity());
                        preferences.setFilterCategoryData(filterCategory);
                        preferences.setFilterTypeData(filterType);
                        callgetAllMenu();
                    }
                });

                tvReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filterType = "";
                        filterCategory = "";
                        rbNonVeg.setChecked(false);
                        rbVeg.setChecked(false);
                        rbBoth.setChecked(false);
                        chkIndian.setChecked(false);
                        chkItalina.setChecked(false);
                        chkChinese.setChecked(false);
                        chkMaxican.setChecked(false);
                        chkPunjabi.setChecked(false);
                        /*Preferences preferences = new Preferences(getActivity());
                        preferences.setFilterCategoryData("");
                        preferences.setFilterTypeData("");
                        callgetAllMenu();*/
                    }
                });

                final TextView tvType = (TextView) alert.findViewById(R.id.tvType);
                final TextView tvCategory = (TextView) alert.findViewById(R.id.tvCategory);
                final LinearLayout linearType = (LinearLayout) alert.findViewById(R.id.linearType);
                final LinearLayout linearCategory = (LinearLayout) alert.findViewById(R.id.linearCategory);

                linearType.setVisibility(View.VISIBLE);
                linearCategory.setVisibility(View.GONE);

                tvType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvType.setBackgroundColor(getResources().getColor(R.color.white));
                        tvCategory.setBackgroundColor(getResources().getColor(R.color.color_ebebeb));

                        linearType.setVisibility(View.VISIBLE);
                        linearCategory.setVisibility(View.GONE);
                    }
                });


                tvCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvCategory.setBackgroundColor(getResources().getColor(R.color.white));
                        tvType.setBackgroundColor(getResources().getColor(R.color.color_ebebeb));

                        linearCategory.setVisibility(View.VISIBLE);
                        linearType.setVisibility(View.GONE);
                    }
                });

            }
        });

        binding.llSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sort_by, null);
                builder.setView(dialogView);
                final AlertDialog alert = builder.show();

                final TextView tvRating = (TextView) alert.findViewById(R.id.tvRating);
                final TextView tvPriceLowHigh = (TextView) alert.findViewById(R.id.tvPriceLowHigh);
                final TextView tvPriceHighLow = (TextView) alert.findViewById(R.id.tvPriceHighLow);
                final TextView tvDeliveryTime = (TextView) alert.findViewById(R.id.tvDeliveryTime);

                if(sortType.equalsIgnoreCase("rate")){
                    setTextViewDrawableColor(tvRating, getResources().getColor(R.color.color_f04937));
                    setTextViewDrawableColor(tvPriceLowHigh, getResources().getColor(R.color.color_777777));
                    setTextViewDrawableColor(tvPriceHighLow, getResources().getColor(R.color.color_777777));
                    setTextViewDrawableColor(tvDeliveryTime, getResources().getColor(R.color.color_777777));
                }else if(sortType.equalsIgnoreCase("LTH")){
                    setTextViewDrawableColor(tvPriceLowHigh, getResources().getColor(R.color.color_f04937));
                    setTextViewDrawableColor(tvRating, getResources().getColor(R.color.color_777777));
                    setTextViewDrawableColor(tvPriceHighLow, getResources().getColor(R.color.color_777777));
                    setTextViewDrawableColor(tvDeliveryTime, getResources().getColor(R.color.color_777777));
                }else if(sortType.equalsIgnoreCase("HTL")){
                    setTextViewDrawableColor(tvPriceHighLow, getResources().getColor(R.color.color_f04937));
                    setTextViewDrawableColor(tvRating, getResources().getColor(R.color.color_777777));
                    setTextViewDrawableColor(tvPriceLowHigh, getResources().getColor(R.color.color_777777));
                    setTextViewDrawableColor(tvDeliveryTime, getResources().getColor(R.color.color_777777));
                }else if(sortType.equalsIgnoreCase("DT")){
                    setTextViewDrawableColor(tvDeliveryTime, getResources().getColor(R.color.color_f04937));
                    setTextViewDrawableColor(tvRating, getResources().getColor(R.color.color_777777));
                    setTextViewDrawableColor(tvPriceLowHigh, getResources().getColor(R.color.color_777777));
                    setTextViewDrawableColor(tvPriceHighLow, getResources().getColor(R.color.color_777777));
                }

                tvRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        setTextViewDrawableColor(tvRating, getResources().getColor(R.color.color_f04937));
                        setTextViewDrawableColor(tvPriceLowHigh, getResources().getColor(R.color.color_777777));
                        setTextViewDrawableColor(tvPriceHighLow, getResources().getColor(R.color.color_777777));
                        setTextViewDrawableColor(tvDeliveryTime, getResources().getColor(R.color.color_777777));
                        sortType = "rate";
                        Preferences preferences = new Preferences(getActivity());
                        preferences.setFilterCategoryData(sortType);
                        pageProduct = 0;
                        callgetAllMenu();
                    }
                });

                tvPriceLowHigh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        setTextViewDrawableColor(tvPriceLowHigh, getResources().getColor(R.color.color_f04937));
                        setTextViewDrawableColor(tvRating, getResources().getColor(R.color.color_777777));
                        setTextViewDrawableColor(tvPriceHighLow, getResources().getColor(R.color.color_777777));
                        setTextViewDrawableColor(tvDeliveryTime, getResources().getColor(R.color.color_777777));
                        sortType = "LTH";
                        Preferences preferences = new Preferences(getActivity());
                        preferences.setFilterCategoryData(sortType);
                        callgetAllMenu();
                    }
                });

                tvPriceHighLow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        setTextViewDrawableColor(tvPriceHighLow, getResources().getColor(R.color.color_f04937));
                        setTextViewDrawableColor(tvRating, getResources().getColor(R.color.color_777777));
                        setTextViewDrawableColor(tvPriceLowHigh, getResources().getColor(R.color.color_777777));
                        setTextViewDrawableColor(tvDeliveryTime, getResources().getColor(R.color.color_777777));
                        sortType = "HTL";
                        Preferences preferences = new Preferences(getActivity());
                        preferences.setFilterCategoryData(sortType);
                        callgetAllMenu();
                    }
                });


                tvDeliveryTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        setTextViewDrawableColor(tvDeliveryTime, getResources().getColor(R.color.color_f04937));
                        setTextViewDrawableColor(tvRating, getResources().getColor(R.color.color_777777));
                        setTextViewDrawableColor(tvPriceLowHigh, getResources().getColor(R.color.color_777777));
                        setTextViewDrawableColor(tvPriceHighLow, getResources().getColor(R.color.color_777777));
                        sortType = "DT";
                        Preferences preferences = new Preferences(getActivity());
                        preferences.setFilterCategoryData(sortType);
                        callgetAllMenu();
                    }
                });
                ImageView ivClose = (ImageView) alert.findViewById(R.id.ivClose);
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
            }
        });

    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        textView.setTextColor(color);
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_filter:
                if (!binding.edtSearchBar.getText().toString().equalsIgnoreCase("")) {
                    if (productBeansArray != null && productBeansArray.size() > 0) {
                        filterProductBeansArray.clear();
                        for (ProductBean wp : productBeansArray) {
                            if (wp.getName().toLowerCase(Locale.getDefault()).contains(binding.edtSearchBar.getText().toString())) {
                                filterProductBeansArray.add(wp);
                            }
                        }
                        homeAdapter.addAllList(filterProductBeansArray);
                    } else
                        CommonUtils.toast(context, "No result found.");
                } else
                    homeAdapter.addAllList(productBeansArray);
                break;

            case R.id.img_cart:
            /*case R.id.tv_proceed:
                Intent intent = new Intent(context, ActivityCart.class);
                startActivityForResult(intent, Const.CART_DATA_UPDATE);
                break;*/
        }
    }

    private void callgetAllMenu() {
        if (CommonUtils.isInternetOn(context)) {

            String search = "";
            if (isSearchMode) {
                search = binding.edtSearchBar.getText().toString().trim();
            }

            pageProduct++;
            if (!homeAdapter.isLoading() && !binding.swiperefresh.isRefreshing() && binding.llProgress.getVisibility() != View.VISIBLE)
                binding.llProgress.setVisibility(View.VISIBLE);
            Observable<GetProductBean<ArrayList<ProductBean>>> signupusers = FetchServiceBase.getFetcherService(context)
                    .getMenu(pageProduct, search, filterCategory, filterType,sortType);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetProductBean<ArrayList<ProductBean>>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("HomeFragment error ", "" + e);
                            binding.llProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(GetProductBean<ArrayList<ProductBean>> getProductBean) {
                            binding.llProgress.setVisibility(View.GONE);
                            if (homeAdapter.isLoading()) {
                                homeAdapter.removeProgress();
                            } else {
                                if (binding.swiperefresh.isRefreshing()) {
                                    binding.swiperefresh.setRefreshing(false);
                                }
                            }
                            if (getProductBean.getResult() != null && getProductBean.getResult().size() > 0) {
                                lastPage = Integer.parseInt(getProductBean.getLast_page());
                                binding.swiperefresh.setVisibility(View.VISIBLE);
                                binding.llNoData.setVisibility(View.GONE);
                                if (homeAdapter.isLoading()) {
                                    homeAdapter.setLoaded();
                                    homeAdapter.addAllList(getProductBean.getResult());
                                } else {
                                    homeAdapter.clearList();
                                    homeAdapter.addAllList(getProductBean.getResult());
                                }

                                cartProductBeansArray.clear();
                                for (Datum datum : cartItem) {

                                    for (ProductBean productBean : homeAdapter.getList()) {
                                        if (datum.getProductId() == productBean.getId()) {
                                            productBean.setQty(datum.getQty());
                                            cartProductBeansArray.add(productBean);
                                        }
                                    }
                                }
                                homeAdapter.notifyDataSetChanged();
                                calculateCartTotal();


                            } else {
                                homeAdapter.clearAll();
                                binding.swiperefresh.setVisibility(View.GONE);
                                binding.llNoData.setVisibility(View.VISIBLE);
                                homeAdapter.removeProgress();
                                homeAdapter.setLoaded();
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }


    private void callgetCart() {
        if (CommonUtils.isInternetOn(context)) {

            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", "" + preferences.getUserDataPref().getId());

            if (!homeAdapter.isLoading() && !binding.swiperefresh.isRefreshing() && binding.llProgress.getVisibility() != View.VISIBLE)
                binding.llProgress.setVisibility(View.VISIBLE);
            Observable<GetCart> signupusers = FetchServiceBase.getFetcherService(context)
                    .getCart(CommonUtils.converRequestBodyFromMap(map));
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetCart>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("Cart error ", "" + e);
                            //binding.llProgress.setVisibility(View.GONE);
                            callgetAllMenu();
                        }

                        @Override
                        public void onNext(GetCart getCart) {
                            //binding.llProgress.setVisibility(View.GONE);
                            if (getCart.getStatus()) {

                                cartItem.clear();
                                cartItem.addAll(getCart.getData());

                            }
                            callgetAllMenu();
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }


    private void callAddToCart(int quantity, final int pos) {
        if (CommonUtils.isInternetOn(context)) {

            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", "" + preferences.getUserDataPref().getId());
            map.put("product_id", "" + homeAdapter.getList().get(pos).getId());
            map.put("qty", "" + quantity);

            binding.llProgress.setVisibility(View.VISIBLE);
            Observable<ModelBean> signupusers = FetchServiceBase.getFetcherService(context)
                    .addToCart(CommonUtils.converRequestBodyFromMap(map));
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("Favourite error ", "" + e);
                            binding.llProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean modelBean) {
                            binding.llProgress.setVisibility(View.GONE);
                            if (modelBean.getStatus()) {

                                CommonUtils.toast(context, modelBean.getMessage());
                                int qty = homeAdapter.getList().get(pos).getQty();
                                qty++;
                                homeAdapter.getList().get(pos).setQty(qty);
                                homeAdapter.notifyDataSetChanged();
                                if (!cartProductBeansArray.contains(homeAdapter.getList().get(pos)))
                                    cartProductBeansArray.add(homeAdapter.getList().get(pos));
                                else {
                                    cartProductBeansArray.get(cartProductBeansArray.indexOf(homeAdapter.getList().get(pos)));
                                }
                                calculateCartTotal();
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }


    private void updateQuantity(final int quantity, final int pos) {
        if (CommonUtils.isInternetOn(context)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", "" + preferences.getUserDataPref().getId());
            map.put("product_id", "" + homeAdapter.getList().get(pos).getId());
            map.put("qty", "" + quantity);

            binding.llProgress.setVisibility(View.VISIBLE);
            Observable<QuantityUpdateResponse> signupusers = FetchServiceBase.getFetcherService(context)
                    .updateQuantity(CommonUtils.converRequestBodyFromMap(map));
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<QuantityUpdateResponse>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("Cart error ", "" + e);
                            binding.llProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(QuantityUpdateResponse quantityUpdateResponse) {
                            binding.llProgress.setVisibility(View.GONE);
                            if (quantityUpdateResponse.getStatus()) {

                                homeAdapter.getList().get(pos).setQty(quantity);
                                homeAdapter.notifyDataSetChanged();
                                if (homeAdapter.getList().get(pos).getQty() == 0) {
                                    removeCartItem(homeAdapter.getList().get(pos).getId());
                                } else {
                                    calculateCartTotal();
                                }
                               /* Datum datum = cartAdapter.getItem(pos);
                                Datum updatedDatum = quantityUpdateResponse.getData();
                                datum.setQty(updatedDatum.getQty());
                                datum.setDiscount(updatedDatum.getDiscount());
                                datum.setCreatedAt(updatedDatum.getCreatedAt());
                                datum.setUpdatedAt(updatedDatum.getUpdatedAt());
                                cartAdapter.putItem(pos, datum);*/

                            }
                        }
                    });
        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    private void removeCartItem(int id) {

        for (ProductBean productBean : cartProductBeansArray) {
            if (productBean.getId() == id) {
                cartProductBeansArray.remove(productBean);
            }
        }
        calculateCartTotal();
    }


    private void calculateCartTotal() {

        int total = 0;
        for (ProductBean productBean : cartProductBeansArray) {
            total += (Integer.parseInt(productBean.getPrice()) * productBean.getQty());
        }

        //binding.tvItemPrice.setText(cartProductBeansArray.size() + " Items | $ " + total);

    }


    @Override
    public void onItemClick(View view, int pos) {
        int quantity = 0;
        switch (view.getId()) {
            case R.id.iv_plus:
                if (homeAdapter.getList().get(pos).getQty() < 100) {
                    callAddToCart(1, pos);
                }
                break;
            case R.id.iv_minus:
                if (homeAdapter.getList().get(pos).getQty() > 0) {
                    quantity = homeAdapter.getList().get(pos).getQty() - 1;
                    updateQuantity(quantity, pos);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Const.CART_DATA_UPDATE) {

                String removeItems = data.getStringExtra(Const.REMOVED_ITEM_IDS);
                String updatedItems = data.getStringExtra(Const.EXTRA_OBJ);

                // Update quantity to "0" if user remove any item from cart
                if (removeItems != null && !removeItems.isEmpty() && cartProductBeansArray.size() > 0) {

                    ArrayList<ProductBean> tempList = new ArrayList<>();
                    tempList.addAll(cartProductBeansArray);
                    for (ProductBean productBean : tempList) {
                        if (removeItems.contains("" + productBean.getId())) {
                            cartProductBeansArray.remove(productBean);
                            productBean.setQty(0);
                            homeAdapter.getList().set(homeAdapter.getList().indexOf(productBean), productBean);
                        }
                    }

                    tempList.clear();
                    tempList = null;
                }

                //Update quantity as per change from cart
                if (updatedItems != null && !updatedItems.isEmpty()) {
                    try {
                        JSONArray jsonArray = new JSONArray(updatedItems);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            int qty = jsonObject.getInt("qty");
                            for (ProductBean productBean : cartProductBeansArray) {
                                if (id == productBean.getId()) {
                                    productBean.setQty(qty);
                                    cartProductBeansArray.set(cartProductBeansArray.indexOf(productBean), productBean);
                                    homeAdapter.getList().set(homeAdapter.getList().indexOf(productBean), productBean);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                homeAdapter.notifyDataSetChanged();
                calculateCartTotal();
            }
        }
    }

    /*public void onSortDialog(View view) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sort_by);

        TextView tvRating = (TextView) dialog.findViewById(R.id.tvRating);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }*/
}
