package com.nvcomputers.ten.presenter;//package com.tentheapp.presenter;
//
//import com.tentheapp.api.GetRestAdapter;
//import com.tentheapp.interfaces.AppCommonCallback;
//import com.tentheapp.model.output.GetPostLikersListResponse;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * Created by Thaparsneh on 5/9/2017.
// */
//
//public class GetPostLikersPresenter {
//    private GetPostLikersCallback getPostLikersCallback;
//    private AppCommonCallback screen;
//
//    public GetPostLikersPresenter(AppCommonCallback callback, GetPostLikersCallback getPostLikersCallback) {
//        this.screen = callback;
//        this.getPostLikersCallback = getPostLikersCallback;
//    }
//
//    public interface GetPostLikersCallback {
//        void postLikersListError(Call<GetPostLikersListResponse> call, Throwable t);
//
//        void onPostlikersListSuccess(Response<GetPostLikersListResponse> response);
//    }
//
//    public void responseCheck(String idPost) {
//        //screen.setProgressBar();
//        Call<GetPostLikersListResponse> response = GetRestAdapter.getRestAdapter(true).postLikersList(idPost);
//        response.enqueue(new Callback<GetPostLikersListResponse>() {
//            @Override
//            public void onResponse(Call<GetPostLikersListResponse> call, retrofit2.Response<GetPostLikersListResponse> response) {
//                //screen.dismiss();
//                getPostLikersCallback.onPostlikersListSuccess(response);
//            }
//
//            @Override
//            public void onFailure(Call<GetPostLikersListResponse> call, Throwable t) {
//                //screen.dismiss();
//                getPostLikersCallback.postLikersListError(call, t);
//            }
//        });
//    }
//}
//
