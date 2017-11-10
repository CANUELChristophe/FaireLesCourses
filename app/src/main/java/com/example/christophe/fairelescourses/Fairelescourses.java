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

        System.out.println("passage onCreate()");
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
                        System.out.println("reponse = " + reponse);
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
                System.out.println("Avant dialog Oui");
                effacerOuiNon.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("J'ai appuyé sur Oui");
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

                System.out.println("Apres dialog Non");
                effacerOuiNon.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("J'ai appuyé sur Non");
                    }
                });
                System.out.println("Sortie dialog");
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
                    else
                    {
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
        System.out.println("passage onPostResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("passage onStart()");
        /* Debut Read */
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
//        ArrayList<ListeCourse> listeCourseListLocal = ListeCourseDAO.getAllRecordsCursorBAD(db);
        ArrayList<ListeCourse> listeCourseListLocal = ListeCourseDAO.getAllRecords(db);
        db.close();

        for (int i = 0;i < listeCourseListLocal.size();i++) {
            listeCourseList.add(listeCourseListLocal.get(i));
        }

        trierListeCourse();

        /* Fin Read */
        db.close();

    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("passage onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* sauvegardee donnees BDD puis fermeture BDD */
        /* debut Write */


        /* Fin Write */

    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("passage onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("passage onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("passage onRestart()");
    }

/*--------------------------------------------------------------*/

    private int ChercheProduit (String produit) {
        for (int i=0; i < listeCourseList.size(); i++){
            System.out.println("getProduit(1) " + i + "<" + listeCourseList.get(i).getProduit() + ">");
            System.out.println("Produit(1) <" + produit + ">");
            if (listeCourseList.get(i).getProduit().equals(produit)) {
                System.out.println("getProduit(2) " + i + "<" + listeCourseList.get(i).getProduit() + ">");
                System.out.println("Produit(2) <" + produit + ">");
                System.out.println("sortie boucle " + i);
                return i;
            }
        }
        return -1;
    };

    private String AjouterProduit (String produit, double quantite, String unite) {
        String resultat = "OK";
        int positionProduit = ChercheProduit(produit);
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        if (positionProduit == -1) {
            listeCourseList.add(new ListeCourse(produit, quantite, unite));
        /* Debut Ajout SQL */
            ListeCourseDAO.insertRecord(db,listeCourseList.get(listeCourseList.size() - 1));
        /* Fin Ajout SQL */
            System.out.println("Add : " + produit + " nb : " + quantite + "unite : " + unite);
            trierListeCourse();
        } else {
            ListeCourse ligneAModifier = listeCourseList.get(positionProduit);
            resultat = ligneAModifier.addQuantite(quantite, unite);
            if (resultat != "OK"){
                return resultat;
            }
        /* Debut Ajout SQL */
            ListeCourseDAO.updateRecord(db,ligneAModifier);
        /* Fin Ajout SQL */
            listeCourseList.set(positionProduit,ligneAModifier);
            System.out.println("Update : " + produit + " nb : " + quantite);
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
                System.out.println("index1 = " + index1 + "produit 1 = " + listeCourseList.get(index1).getProduit().toString() + " produit 2 = " + listeCourseList.get(index2).getProduit().toString() + "compareTo = " + listeCourseList.get(index1).getProduit().compareTo(listeCourseList.get(index2).getProduit()));

                if (listeCourseList.get(index1).getProduit().compareTo(listeCourseList.get(index2).getProduit()) > 0 ){ /* index1 > index2*/
                    System.out.println("inversion " + listeCourseList.get(index1).getProduit() + " - " + listeCourseList.get(index2).getProduit());
                    listeCoursePoubelle = listeCourseList.get(index1);
                    listeCourseList.set(index1,listeCourseList.get(index2));
                    listeCourseList.set(index2,listeCoursePoubelle);
                    change = true;
                }
            }
        } while (change == true);
        System.out.println("-------------------------------");
        for (index1=0;index1<listeCourseList.size();index1++){
            System.out.println(listeCourseList.get(index1).getProduit());
        }
        System.out.println("-------------------------------");
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
