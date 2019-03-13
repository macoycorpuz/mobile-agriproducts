package thesis.agriproducts.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import thesis.agriproducts.AppController;
import thesis.agriproducts.view.fragment.AccountAdminFragment;
import thesis.agriproducts.view.fragment.AccountFragment;
import thesis.agriproducts.view.fragment.AdminFragment;
import thesis.agriproducts.view.fragment.HomeFragment;
import thesis.agriproducts.view.fragment.InboxFragment;
import thesis.agriproducts.view.fragment.MessagesFragment;
import thesis.agriproducts.view.fragment.MyProductsFragment;
import thesis.agriproducts.view.fragment.ProductDetailsFragment;
import thesis.agriproducts.view.fragment.SellFragment;
import thesis.agriproducts.view.fragment.UserDetailsFragment;

//Class for usable functions
public class Utils {

    //region Initialize
    private static Utils utils;

    public static Utils getUtils() {

        if (null == utils) {
            utils = new Utils();
        }
        return utils;
    }
    //endregion

    //region UI Interaction
    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    public static void dismissProgressDialog(ProgressDialog pDialog) {
        if (pDialog != null) pDialog.dismiss();
    }

    public static void showProgress(final boolean show, final View progressView, final View goneForm) {
        int shortAnimTime = AppController.getInstance().resources.getInteger(android.R.integer.config_shortAnimTime);

        goneForm.setVisibility(show ? View.GONE : View.VISIBLE);
        goneForm.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                goneForm.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //endregion

    //region Authentication
    public static  boolean isEmptyFields(String... fields) {
        for(String f : fields) {
            if(f.isEmpty()) return false;
        }
        return true;
    }

    public static  boolean isEmailValid(String email) {
        if (email == null) return false;
        return (!TextUtils.isEmpty(email)) && (email.contains("@"));
    }

    public static  boolean isPasswordValid(String password) {
        return (!TextUtils.isEmpty(password));
    }

    public static  boolean isPasswordGreaterThanEight(String password) {
        return (password.length() > 8);
    }
    //endregion

    //region Real Path
    public static  String getRealPathFromURI(Context context, Uri contentUri) {
        String result;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
        } catch (Exception ex) {
            result = ex.getMessage();
        }
        return result;
    }
    //endregion

    //region Fragment Util
    private static int position = 0;
    private static String CURRENT_TAG = null;

    public static void switchContent(FragmentActivity baseActivity, int id, String TAG) {

        Fragment fragment;
        FragmentManager fragmentManager = baseActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!TAG.equals(CURRENT_TAG)) {
            switch (TAG) {
                case Tags.HOME_FRAGMENT:
                    fragment = new HomeFragment();
                    break;
                case Tags.SELL_FRAGMENT:
                    fragment = new SellFragment();
                    break;
                case Tags.INBOX_FRAGMENT:
                    fragment = new InboxFragment();
                    break;
                case Tags.MY_PRODUCTS_FRAGMENT:
                    fragment = new MyProductsFragment();
                    break;
                case Tags.ACCOUNT_FRAGMENT:
                    fragment = new AccountFragment();
                    break;
                case Tags.PRODUCT_DETAILS_FRAGMENT:
                    fragment = new ProductDetailsFragment();
                    ((ProductDetailsFragment) fragment).setPosition(position);
                    break;
                case Tags.MESSAGES_FRAGMENT:
                    fragment = new MessagesFragment();
                    ((MessagesFragment) fragment).setPosition(position);
                    break;
                case Tags.USER_DETAILS_FRAGMENT:
                    fragment = new UserDetailsFragment();
                    ((UserDetailsFragment) fragment).setPosition(position);
                    break;
                case Tags.USERS_FRAGMENT:
                    fragment = new AdminFragment();
                    ((AdminFragment) fragment).setTag(TAG);
                case Tags.PRODUCTS_FRAGMENT:
                    fragment = new AdminFragment();
                    ((AdminFragment) fragment).setTag(TAG);
                    break;
                case Tags.ACCOUNT_ADMIN_FRAGMENT:
                    fragment = new AccountAdminFragment();
                    break;
                default:
                    fragment = null;
                    break;
            }

            CURRENT_TAG = TAG;
            transaction.replace(id, fragment, TAG);
            transaction.commit();
        }
    }

    public static void setPosition(int position) {
        Utils.position = position;
    }
    //endregion

}