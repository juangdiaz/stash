package com.juangdiaz.apptest.presenter;

import android.content.Context;
import android.os.AsyncTask;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;
import com.juangdiaz.apptest.model.User;
import com.juangdiaz.apptest.repository.UserRepository;
import com.juangdiaz.apptest.view.UserView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class UserPresenterImpl implements UserPresenter{

    private UserView view;
    private UserRepository userRepository;
    private List<User> userList = new ArrayList<>();
    private Context context;



    public UserPresenterImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public void loadUserDetails() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute(){

                view.showLoadingLayout();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                //Load from contacts
                Query q = Contacts.getQuery();
                q.hasPhoneNumber();
                q.include(Contact.Field.DisplayName, Contact.Field.Email, Contact.Field.PhoneNormalizedNumber);
                List<Contact> contacts = q.find();
                User user;


                for(Contact contact : contacts ){
                    if(contact.getPhoneNumbers().size() > 0) {
                        user = new User();
                        user.setFirstName(contact.getDisplayName());
                        user.setPhoneNumb(contact.getPhoneNumbers().get(0).getNormalizedNumber());
                        if(contact.getEmails().size() > 0){
                            user.setEmail(contact.getEmails().get(0).getAddress());
                        } else {
                            user.setEmail("");
                        }

                        if(user.getPhoneNumb() != null) {
                            userList.add(user);
                        }
                    }

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result)
            {
                view.displayUsers(userList);
                view.hideLoadingLayout();
            }
        }.execute((Void[]) null);
    }


    @Override
    public void setView(Context context, UserView view) {

        this.view = view;
        this.context = context;
        loadUserDetails();
    }

}
