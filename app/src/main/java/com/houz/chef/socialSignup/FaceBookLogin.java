package com.houz.chef.socialSignup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.houz.chef.R;
import com.houz.chef.interfaces.FacebookInterface;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;


public class FaceBookLogin {

    private CallbackManager callbackManager;

    public FaceBookLogin(final Context context, final FacebookInterface facebookInterface) {

//        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                // USre ID = FB Id

                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {

                                if (loginResult.getAccessToken() != null) {

                                    Set<String> declinedPermissions = loginResult.getRecentlyDeniedPermissions();

                                    if (declinedPermissions.size() > 0 && (declinedPermissions.contains("email") || declinedPermissions.contains("user_friends"))) {
                                        if (declinedPermissions.contains("email"))
                                            Toast.makeText(context, "Please allow email permission in facebook", Toast.LENGTH_LONG).show();

//                                        else if (declinedPermissions.contains("user_friends"))
//                                            Toast.makeText(context, "Please allow User Friend permission in facebook", Toast.LENGTH_LONG).show();
                                    }

                                    try {

                                        HashMap<String, String> param = new HashMap<>();
//                                        if (declinedPermissions.size() > 0 && (declinedPermissions.contains("email"))) {
                                            param.put("email", String.valueOf(object.getString("email")));
//                                        }
                                        param.put("firstName", String.valueOf(object.getString("first_name")));
                                        param.put("lastName", String.valueOf(object.getString("last_name")));
                                        param.put("socialId", loginResult.getAccessToken().getUserId());

                                        param.put("profileImageURL", String.valueOf("http://graph.facebook.com/" + String.valueOf(object.getString("id")) + "/picture?type=large"));

                                        facebookInterface.success(param);
                                        getFriendList(AccessToken.getCurrentAccessToken(), facebookInterface);

//                                        LoginManager.getInstance().logOut();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name,picture{url}");
                request.setParameters(parameters);
                request.executeAsync();

                // App code
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, context.getResources().getString(R.string.toast_login_fail), Toast.LENGTH_LONG).show();
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(context, context.getResources().getString(R.string.toast_login_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getFriendList(AccessToken accessToken, final FacebookInterface facebookInterface) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,picture{url}");

        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                accessToken,
                //AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        ArrayList<Object> list = new ArrayList<>();
                        try {
                            JSONArray rawName = response.getJSONObject().getJSONArray("data");
                            try {
                                for (int l = 0; l < rawName.length(); l++) {
                                    Emailfriend bean = new Emailfriend();
                                    bean.setFname(String.valueOf(rawName.getJSONObject(l).getString("name")));
                                    bean.setPhoto("http://graph.facebook.com/ " + String.valueOf(rawName.getJSONObject(l).getString("id")) + "/picture?type=large");
                                    bean.setFbid(String.valueOf(rawName.getJSONObject(l).getString("id")));

                                    if ((bean.getFbid() != null) && !bean.getFbid().equals(""))
                                        list.add(bean);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        facebookInterface.onFbFrdFetch(list);
                    }
                }
        ).executeAsync();

    }

    public void faceBookManager(Context context) {
        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile", "email", "user_friends"));
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}