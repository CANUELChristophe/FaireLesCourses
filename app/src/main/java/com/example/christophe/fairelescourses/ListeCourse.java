package com.example.christophe.fairelescourses;

/**
 * Created by christophe on 04/11/2017.
 */

public class ListeCourse {

    private String produit;
    private double quantite;
    private String unite;
    private boolean etat;

    public ListeCourse(String produit) {
        this(produit, 1);
    }

    public ListeCourse(String produit, double quantite) {
        this(produit, quantite, " ");
    }

    public ListeCourse(String produit, double quantite, String unite) {
        this(produit, quantite, unite, false);
    }

    public ListeCourse(String produit, double quantite, String unite, boolean checkBox) {
        this.produit = produit;
        this.quantite = quantite;
        this.unite = unite;
        this.etat = checkBox;
    }

    public String getProduit() {
        return produit;
    }

    public double getQuantite() {
        return quantite;
    }

    public String getQuantiteString() {
        if (unite == "Kg") {
            return Double.toString(this.quantite);
        }
        return Integer.toString((int)this.quantite);
    }

    public String getUnite() {
        return this.unite;
    }

    public boolean isEtat() {
        return this.etat;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public void switchEtat() {
        this.etat = !this.etat;
    }

    public String addQuantite(double quantite, String unite) {
        double quantiteBaseEnGramme = 0;
        double quantiteEnGramme = 0;
        double quantiteCalculee = 0;
        if (!((this.getUnite().equals(" ")&(unite.equals("g") | unite.equals("Kg")))
                | ((this.getUnite().equals("g") | this.getUnite().equals("Kg"))&(unite.equals(" "))))) {
            if (unite.equals(" ")) {
                quantiteCalculee = this.getQuantite() + quantite;
            }
            else
            {
                quantiteBaseEnGramme = this.getQuantite();
                if (this.getUnite().equals("g")) {
                    quantiteBaseEnGramme = this.getQuantite();          /* g  */
                }
                else {
                    quantiteBaseEnGramme = this.getQuantite() * 1000;   /* Kg */
                }
                if (unite.equals("g")) {
                    quantiteEnGramme = quantite;                        /* g  */
                }
                else {
                    quantiteEnGramme = quantite * 1000;                 /* Kg */
                }
                quantiteCalculee = quantiteBaseEnGramme + quantiteEnGramme;
                if (quantiteCalculee >= 1000) {                         /* on est sur du Kilo */
                    quantiteCalculee = quantiteCalculee / 1000;
                    this.setUnite("Kg");
                }
                else {
                    this.setUnite("g");
                }
            }
            this.quantite = quantiteCalculee;
            return "OK";
        }
        else {
            return "Unite incompatible";
        }
    }

}
