package com.chezacasino.casino.networking;

import com.chezacasino.casino.models.MessageModel;
import com.chezacasino.casino.models.TrialsModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface JsonPlaceHolderInterface {
    @FormUrlEncoded
    @POST("api/stk")
    Call<MessageModel> initiate(
            @Field("phone") String phone,
            @Field("amount") String am
    );
    @FormUrlEncoded
    @POST("api/getcasino")
    Call<TrialsModel> pTrials(
            @Field("phone") String phone
    );
    @FormUrlEncoded
    @POST("api/reducetrials")
    Call<MessageModel> reduceT(
            @Field("phone") String phone
    );
    @FormUrlEncoded
    @POST("api/insertcasino")
    Call<MessageModel> casiN(
            @Field("phone") String phone,
            @Field("amount") String amount
    );
}
