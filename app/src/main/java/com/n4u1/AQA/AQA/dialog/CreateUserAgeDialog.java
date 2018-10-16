package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.n4u1.AQA.AQA.R;

public class CreateUserAgeDialog extends DialogFragment {

    public CreateUserAgeDialog() {}

    public interface CreateUserAgeDialogListener {
        public void choiceItemCallback(String string);
    }

    CreateUserAgeDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CreateUserAgeDialogListener) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("※ 몇년도에 태어나셨나요?")
                .setItems(R.array.createUserAge, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result = "";
                        switch (which) {
                            case 0 : result ="1920"; break;
                            case 1 : result ="1921"; break;
                            case 2 : result ="1922"; break;
                            case 3 : result ="1923"; break;
                            case 4 : result ="1924"; break;
                            case 5 : result ="1925"; break;
                            case 6 : result ="1926"; break;
                            case 7 : result ="1927"; break;
                            case 8 : result ="1928"; break;
                            case 9 : result ="1929"; break;
                            case 10 : result ="1930"; break;
                            case 11 : result ="1931"; break;
                            case 12 : result ="1932"; break;
                            case 13 : result ="1933"; break;
                            case 14 : result ="1934"; break;
                            case 15 : result ="1935"; break;
                            case 16 : result ="1936"; break;
                            case 17 : result ="1937"; break;
                            case 18 : result ="1938"; break;
                            case 19 : result ="1939"; break;
                            case 20 : result ="1940"; break;
                            case 21 : result ="1941"; break;
                            case 22 : result ="1942"; break;
                            case 23 : result ="1943"; break;
                            case 24 : result ="1944"; break;
                            case 25 : result ="1945"; break;
                            case 26 : result ="1946"; break;
                            case 27 : result ="1947"; break;
                            case 28 : result ="1948"; break;
                            case 29 : result ="1949"; break;
                            case 30 : result ="1950"; break;
                            case 31 : result ="1951"; break;
                            case 32 : result ="1952"; break;
                            case 33 : result ="1953"; break;
                            case 34 : result ="1954"; break;
                            case 35 : result ="1955"; break;
                            case 36 : result ="1956"; break;
                            case 37 : result ="1957"; break;
                            case 38 : result ="1958"; break;
                            case 39 : result ="1959"; break;
                            case 40 : result ="1960"; break;
                            case 41 : result ="1961"; break;
                            case 42 : result ="1962"; break;
                            case 43 : result ="1963"; break;
                            case 44 : result ="1964"; break;
                            case 45 : result ="1965"; break;
                            case 46 : result ="1966"; break;
                            case 47 : result ="1967"; break;
                            case 48 : result ="1968"; break;
                            case 49 : result ="1969"; break;
                            case 50 : result ="1970"; break;
                            case 51 : result ="1971"; break;
                            case 52 : result ="1972"; break;
                            case 53 : result ="1973"; break;
                            case 54 : result ="1974"; break;
                            case 55 : result ="1975"; break;
                            case 56 : result ="1976"; break;
                            case 57 : result ="1977"; break;
                            case 58 : result ="1978"; break;
                            case 59 : result ="1979"; break;
                            case 60 : result ="1980"; break;
                            case 61 : result ="1981"; break;
                            case 62 : result ="1982"; break;
                            case 63 : result ="1983"; break;
                            case 64 : result ="1984"; break;
                            case 65 : result ="1985"; break;
                            case 66 : result ="1986"; break;
                            case 67 : result ="1987"; break;
                            case 68 : result ="1988"; break;
                            case 69 : result ="1989"; break;
                            case 70 : result ="1990"; break;
                            case 71 : result ="1991"; break;
                            case 72 : result ="1992"; break;
                            case 73 : result ="1993"; break;
                            case 74 : result ="1994"; break;
                            case 75 : result ="1995"; break;
                            case 76 : result ="1996"; break;
                            case 77 : result ="1997"; break;
                            case 78 : result ="1998"; break;
                            case 79 : result ="1999"; break;
                            case 80 : result ="2000"; break;
                            case 81 : result ="2001"; break;
                            case 82 : result ="2002"; break;
                            case 83 : result ="2003"; break;
                            case 84 : result ="2004"; break;
                            case 85 : result ="2005"; break;
                            case 86 : result ="2006"; break;
                            case 87 : result ="2007"; break;
                        }
                        mListener.choiceItemCallback(result);
                    }
                });
        return builder.create();
    }
}
