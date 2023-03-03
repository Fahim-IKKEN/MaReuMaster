package com.openclassrooms.mareu.core;

/**
 * Le modèle MVP (Model-View-Presenter) est un modèle de conception de logiciel couramment
 * utilisé dans le développement Android.
 *
 * Il permet :
 *  - pour éviter une classe d'activités composée de milliers de lignes
 *  - séparation des préoccupations, séparation de la couche d'interface utilisateur de la logique métier (échange facile de vues)
 *  - pour une meilleure séparation des préoccupations et une meilleure testabilité
 *  - une application facilement extensible et maintenable
 *
 * Trois parties distinctes du motif sont :
 *
 * - le Présentateur agit comme un médiateur entre la Vue et le Modèle :
 * il s'occupe essentiellement de récupérer  les données du modèle et met à jour la vue avec ces données,
 * ainsi que la gestion des entrées des utilisateurs et la mise à jour du modèle en conséquence
 * Un présentateur est attaché en permanence à une vue donnée.
 *
 * - la vue affiche les données à l'utilisateur. La vue est essentiellement une activité (ou un fragment).
 *
 * - le modèle représente les données de l'application et la logique métier.
 *
 *
 */

public interface SimpleMvp {

    // The Model interface
    interface Model {
        // super interface Model, not used in this app, but keep in mind that Model is
        // always needed in the MVP pattern
    }

    // The View interface that is implemented indirectly by the Activity (or Fragment)
    // Generic type T is the Presenter type
    interface View<T> {

        // A given presenter need to be attached to the view :
        // - the view will need to call the presenter's methods when the user interacts with the UI
        // - the view is called by the presenter to update the UI when needed
        void attachPresenter(T presenter);

    }

    // The Presenter interface
    // - call the view's methods to update the UI
    // - interact with the model to retrieve and save data
    interface Presenter {

        // the presenter will need to gather some initial data from the model when it is created
        void init();

    }
}

