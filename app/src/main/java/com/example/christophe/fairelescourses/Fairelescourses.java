package com.example.christophe.fairelescourses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Fairelescourses  extends AppCompatActivity {

    private LinearLayout linearLayoutFaireLesCourses;

    private LinearLayout linearLayoutSaisie;
    private EditText editTextProduitSaisie;
    private EditText editTextQuantiteSaisie;
    private LinearLayout linearLayoutUniteSaisie;
    private TextView textViewUniteSaisie;
    private Button buttonAddCourse;

    private ListView listViewCourse;

    private LinearLayout linearLayoutButton;
    private Button buttonMajCourse;
    private Button buttonEffacerCourse;

    /* Debut private */

    /* Fin Private */

    private SQLiteHelper mySQLiteHelper;

    private final ArrayList<ListeCourse> listeCourseList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faire_les_courses);

        mySQLiteHelper = new SQLiteHelper(this);

        linearLayoutSaisie = (LinearLayout) findViewById(R.id.LinearLayoutSaisie);
        editTextProduitSaisie = (EditText) findViewById(R.id.editTextProduitSaisie);
        editTextQuantiteSaisie = (EditText) findViewById(R.id.editTextQuantiteSaisie);
        linearLayoutUniteSaisie = (LinearLayout) findViewById(R.id.LinearLayoutUniteSaisie);
        textViewUniteSaisie = (TextView) findViewById(R.id.textViewUniteSaisie);
        buttonAddCourse = (Button) findViewById(R.id.buttonAddCourse);

        listViewCourse = (ListView) findViewById(R.id.ListViewCourse);

        linearLayoutButton = (LinearLayout) findViewById(R.id.LinearLayoutButton);
        buttonMajCourse = (Button) findViewById(R.id.buttonMajCourse);
        buttonEffacerCourse = (Button) findViewById(R.id.buttonEffacerCourse);

        /* Debut Read */

        /* Fin Read */

        final ListeCourseAdapter listeCourseAdapter = new ListeCourseAdapter(this, listeCourseList);

        linearLayoutUniteSaisie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] uniteQuestion = {"Pièce","Gramme","Kilo"};
                final CharSequence[] uniteReponseText = {" ","g","Kg"};
                AlertDialog.Builder AlerteDialogBuilderUnite = new AlertDialog.Builder(Fairelescourses.this);
                AlerteDialogBuilderUnite.setTitle("Quelle unité ?");
                AlerteDialogBuilderUnite.setItems(uniteQuestion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int reponse) {
                        textViewUniteSaisie.setText(uniteReponseText[reponse]);
                    }
                });
                AlertDialog alert = AlerteDialogBuilderUnite.create();
                alert.show();
            };
        });

        buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultat = "KO";
                if (editTextQuantiteSaisie.getText().toString().isEmpty() | IsNumeric(editTextQuantiteSaisie.getText().toString())) {
                    if (textViewUniteSaisie.getText().toString() == ""){
                        textViewUniteSaisie.setText(" ");
                    }
                    if (!editTextProduitSaisie.getText().toString().equals("")) {
                        double quantite;
                        if (editTextQuantiteSaisie.getText().toString().equals("")) {
                            quantite = 1;
                        } else {
                            quantite = Integer.parseInt(editTextQuantiteSaisie.getText().toString());
                        };
                        resultat = AjouterProduit(editTextProduitSaisie.getText().toString(), quantite, textViewUniteSaisie.getText().toString());
                    };
                    if (resultat == "Unite incompatible") {
                        textViewUniteSaisie.setText("");
                        editTextProduitSaisie.requestFocus();
                    }
                    else {
                    listeCourseAdapter.notifyDataSetChanged();
         /* retirer le clavier virtuel */
                /*    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);*/
                /*    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);*/
         /* vider produit et quantite */
                    editTextProduitSaisie.setText("");
                    editTextQuantiteSaisie.setText("");
                    textViewUniteSaisie.setText("");
                    editTextProduitSaisie.requestFocus();
                    }
                }
                else
                {
                    editTextQuantiteSaisie.setText("");
                    editTextQuantiteSaisie.requestFocus();
                }
            }
        });

        buttonEffacerCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder effacerOuiNon = new AlertDialog.Builder(v.getContext());
                effacerOuiNon.setTitle("Faire les courses");
                effacerOuiNon.setMessage("Voulez vous vider la liste?");
                effacerOuiNon.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                /* Debut SQL ligne */
                        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
                        ListeCourseDAO.deleteAllRecord(db);
                        db.close();
                /* Fin SQL ligne */
                        listeCourseList.clear();
                        listeCourseAdapter.notifyDataSetChanged();
                        editTextProduitSaisie.setText("");
                        editTextQuantiteSaisie.setText("");
                        editTextProduitSaisie.requestFocus();
                    };
                });

                effacerOuiNon.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });
                AlertDialog Builder = effacerOuiNon.create();
                effacerOuiNon.show();

            }
        });

        buttonMajCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
                for (int i = 0;i < listeCourseList.size();){
                    if (listeCourseList.get(i).isEtat() == true) {
                        ListeCourseDAO.updateRecord(db,listeCourseList.get(i));
                        listeCourseList.remove(i);
                    }
                    else {
                        i++;
                    }
                }
                /* Debut SQL ligne */
                ListeCourseDAO.deleteRecordCHECKBOX(db);
                /* Fin SQL ligne */
                listeCourseAdapter.notifyDataSetChanged();
                db.close();
            }
        });

        listViewCourse.setAdapter(listeCourseAdapter);
    }

/*--------------------------------------------------------------*/

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /* Debut Read SQL */
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
        ArrayList<ListeCourse> listeCourseListLocal = ListeCourseDAO.getAllRecords(db);
        db.close();

        for (int i = 0;i < listeCourseListLocal.size();i++) {
            listeCourseList.add(listeCourseListLocal.get(i));
        }
        trierListeCourse();
        /* Fin Read SQL */
        db.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

/*--------------------------------------------------------------*/

    private int ChercheProduit (String produit) {
        for (int i=0; i < listeCourseList.size(); i++){
            if (listeCourseList.get(i).getProduit().equals(produit)) {
        /* Produit trouvé */
                return i;
            }
        }
        /* Produit non trouvé */
        return -1;
    };

    private String AjouterProduit (String produit, double quantite, String unite) {
        String resultat = "KO";
        int positionProduit = ChercheProduit(produit);
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        if (positionProduit == -1) {
            listeCourseList.add(new ListeCourse(produit, quantite, unite));
        /* Debut Ajout SQL */
            ListeCourseDAO.insertRecord(db,listeCourseList.get(listeCourseList.size() - 1));
        /* Fin Ajout SQL */
            System.out.println("Add : " + produit + " nb : " + quantite + "unite : " + unite);
            trierListeCourse();
        }
        else {
            ListeCourse ligneAModifier = listeCourseList.get(positionProduit);
            resultat = ligneAModifier.addQuantite(quantite, unite);
            if (resultat != "OK"){
                return resultat;
            }
        /* Debut Ajout SQL */
            ListeCourseDAO.updateRecord(db,ligneAModifier);
        /* Fin Ajout SQL */
            listeCourseList.set(positionProduit,ligneAModifier);
        };
        db.close();
        return resultat;
    };

    private void trierListeCourse() {
        int index1 = 0;
        int index2 = 1;
        ListeCourse listeCoursePoubelle;
        boolean change;
        do {
            change = false;
            for (index1=0, index2=index1 +1; index2 < listeCourseList.size();index1++, index2++){
                if (listeCourseList.get(index1).getProduit().compareTo(listeCourseList.get(index2).getProduit()) > 0 ){ /* index1 > index2*/
                    listeCoursePoubelle = listeCourseList.get(index1);
                    listeCourseList.set(index1,listeCourseList.get(index2));
                    listeCourseList.set(index2,listeCoursePoubelle);
                    change = true;
                }
            }
        } while (change == true);
        for (index1=0;index1<listeCourseList.size();index1++){
            System.out.println(listeCourseList.get(index1).getProduit());
        }
    };

    private boolean IsNumeric(String chaine) {
        try {
            Integer.parseInt(chaine);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}
