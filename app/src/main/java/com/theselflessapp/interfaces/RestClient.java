package com.theselflessapp.interfaces;


import com.theselflessapp.modal.CheckInCheckOutPOJO;
import com.theselflessapp.modal.EditProfilePOJO;
import com.theselflessapp.modal.GetUserDataPOJO;
import com.theselflessapp.modal.LogInPOJO;
import com.theselflessapp.modal.MemoryUpdatePOJO;
import com.theselflessapp.modal.RecentCheckInPOJO;
import com.theselflessapp.modal.RemoveSnapMemoryPOJO;
import com.theselflessapp.modal.SignUpPOJO;
import com.theselflessapp.modal.SnapAMemoryPOJO;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by ourdesignz on 22/9/16.
 */
public interface RestClient {

    @POST("login.php?")
    Call<LogInPOJO> logInRequest(@Query("email") String email,
                                 @Query("password") String password,
                                 @Query("device_token") String device_token);

    @Multipart
    @POST("signup.php?")
    Call<SignUpPOJO> signUpRequest(@Query("username") String username,
                                   @Query("email") String email,
                                   @Query("password") String password,
                                   @Query("country") String country,
                                   @Query("gender") String gender,
                                   @Part MultipartBody.Part image);

    @Multipart
    @POST("editprofile.php?")
    Call<EditProfilePOJO> editProfileRequest(@Query("id") String id,
                                             @Query("username") String username,
                                             @Query("email") String email,
                                             @Query("password") String password,
                                             @Query("country") String country,
                                             @Query("gender") String gender,
                                             @Part MultipartBody.Part image);

    @POST("editprofile.php?")
    Call<EditProfilePOJO> editProfileRequest2(@Query("id") String id,
                                              @Query("username") String username,
                                              @Query("email") String email,
                                              @Query("password") String password,
                                              @Query("country") String country,
                                              @Query("gender") String gender,
                                              @Query("image") String image);

    @POST("chk_expiry.php?")
    Call<RemoveSnapMemoryPOJO> removeSnapMemoryRequest();

    @GET("get_data.php?")
    Call<GetUserDataPOJO> getUserDataRequest(@Query("id") String id);

    @POST("checkin_checkout.php?")
    Call<CheckInCheckOutPOJO> checkInCheckOutRequest(@Query("id") String id,
                                                     @Query("lat") String latitude,
                                                     @Query("lng") String longitude,
                                                     @Query("status") String status);

    @Multipart
    @POST("snap_mem.php?")
    Call<SnapAMemoryPOJO> snapAMemoryRequest(@Query("userid") String userid,
                                             @Part MultipartBody.Part image);

    @POST("recent_checkin.php?")
    Call<RecentCheckInPOJO> recentCheckInRequest(@Query("userid") String userid);

    @POST("memory_update.php?")
    Call<MemoryUpdatePOJO> memoryUpdateRequest(@Query("userid") String userid);

}
