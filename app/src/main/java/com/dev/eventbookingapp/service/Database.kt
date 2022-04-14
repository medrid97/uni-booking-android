package com.dev.eventbookingapp.service

import android.util.Log
import com.dev.eventbookingapp.App
import com.dev.eventbookingapp.models.Event
import com.dev.eventbookingapp.models.User
import com.dev.eventbookingapp.models.Venue
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

abstract class Database {

    companion object {
        fun getUserData(uid: String): Task<DocumentSnapshot> {
            return FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .get()
        }

        fun createUser(uid: String, email: String): Task<Void> {
            val data = User.toMap(uid, email, false)
            return FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .set(data)
        }

        fun createAccount(email: String, password: String): Task<AuthResult> {
            val auth = FirebaseAuth.getInstance()
            return auth.createUserWithEmailAndPassword(email, password)
        }

        fun userAuth(email: String, password: String): Task<AuthResult> {
            return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        }

        fun addVenue(venue: Venue): Task<Void> {
            return FirebaseFirestore
                .getInstance()
                .collection("venues")
                .document()
                .set(Venue.toMap(venue))
        }

        fun subscribeToVenues(
            onAdded: (QueryDocumentSnapshot) -> Unit,
            onModified: (QueryDocumentSnapshot) -> Unit,
            onRemoved: (QueryDocumentSnapshot) -> Unit
        ): ListenerRegistration {
            return FirebaseFirestore
                .getInstance()
                .collection("venues")
                .addSnapshotListener { snapshot, error ->
                    if (snapshot !== null && error === null) {
                        for (dc in snapshot.documentChanges) {
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> {
                                    onAdded(dc.document)
                                }
                                DocumentChange.Type.MODIFIED -> {
                                    onModified(dc.document)
                                }
                                DocumentChange.Type.REMOVED -> {
                                    onRemoved(dc.document)
                                }
                            }
                        }
                    }
                }
        }

        fun subscribeToRequests(
            onAdded: (QueryDocumentSnapshot) -> Unit,
            onModified: (QueryDocumentSnapshot) -> Unit,
            onRemoved: (QueryDocumentSnapshot) -> Unit
        ): ListenerRegistration {
            return FirebaseFirestore
                .getInstance()
                .collection("requests")
                .addSnapshotListener { snapshot, error ->
                    if (snapshot !== null && error === null) {
                        for (dc in snapshot.documentChanges) {
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> {
                                    onAdded(dc.document)
                                }
                                DocumentChange.Type.MODIFIED -> {
                                    onModified(dc.document)
                                }
                                DocumentChange.Type.REMOVED -> {
                                    onRemoved(dc.document)
                                }
                            }
                        }
                    }
                }
        }

        fun submitEventRequest(request: Event): Task<Void> {
            return FirebaseFirestore
                .getInstance()
                .collection("requests")
                .document(request.uid)
                .set(request.toMap())
                .addOnSuccessListener {
                    FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document(App.user!!.uid)
                        .collection("requests")
                        .document(request.uid)
                        .set(request.toMap())
                }
        }

        fun acceptEventRequest(request: Event): Task<Void> {

            request.state = "ACCEPTED"

            return FirebaseFirestore
                .getInstance()
                .collection("events")
                .document(request.uid)
                .set(request.toMap())
                .addOnSuccessListener {
                    FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document(request.user)
                        .collection("requests")
                        .document(request.uid)
                        .update(request.toMap())
                }
                .addOnSuccessListener {
                    FirebaseFirestore
                        .getInstance()
                        .collection("requests")
                        .document(request.uid)
                        .delete()
                }
        }

        fun refuseEventRequest(request: Event): Task<Void> {
            request.state = "REFUSED"

            return FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(request.user)
                .collection("requests")
                .document(request.uid)
                .update(request.toMap())
                .addOnSuccessListener {
                    FirebaseFirestore
                        .getInstance()
                        .collection("requests")
                        .document(request.uid)
                        .delete()
                }

        }

        fun subscribeToEvents(
            onAdded: (QueryDocumentSnapshot) -> Unit,
            onModified: (QueryDocumentSnapshot) -> Unit,
            onRemoved: (QueryDocumentSnapshot) -> Unit
        ): ListenerRegistration {
            return FirebaseFirestore
                .getInstance()
                .collection("events")
                .addSnapshotListener { snapshot, error ->
                    if (snapshot !== null && error === null) {
                        for (dc in snapshot.documentChanges) {
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> {
                                    onAdded(dc.document)
                                }
                                DocumentChange.Type.MODIFIED -> {
                                    onModified(dc.document)
                                }
                                DocumentChange.Type.REMOVED -> {
                                    onRemoved(dc.document)
                                }
                            }
                        }
                    }
                }
        }

        fun subscribeToParticipant(
            event: Event,
            onAdded: (QueryDocumentSnapshot) -> Unit,
            onModified: (QueryDocumentSnapshot) -> Unit,
            onRemoved: (QueryDocumentSnapshot) -> Unit
        ): ListenerRegistration {
            return FirebaseFirestore
                .getInstance()
                .collection("events")
                .document(event.uid)
                .collection("attendees")
                .addSnapshotListener { snapshot, error ->
                    if (snapshot !== null && error === null) {
                        for (dc in snapshot.documentChanges) {

                            Log.d("fire", "${snapshot.metadata}")

                            when (dc.type) {
                                DocumentChange.Type.ADDED -> {
                                    onAdded(dc.document)
                                }
                                DocumentChange.Type.MODIFIED -> {
                                    onModified(dc.document)
                                }
                                DocumentChange.Type.REMOVED -> {
                                    onRemoved(dc.document)
                                }
                            }
                        }
                    }
                }
        }

        fun participateInEvent(user: User, event: Event): Task<Void> {
            return FirebaseFirestore
                .getInstance()
                .collection("events")
                .document(event.uid)
                .collection("attendees")
                .document(user.uid)
                .set(mapOf(user.uid to "user"))
                .addOnSuccessListener {
                    FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document(user.uid)
                        .collection("participation")
                        .document(event.uid)
                        .set(mapOf(event.uid to "event"))
                }
        }

        fun removeParticipationFromEvent(user: User, event: Event): Task<Void> {
            return FirebaseFirestore
                .getInstance()
                .collection("events")
                .document(event.uid)
                .collection("attendees")
                .document(user.uid)
                .delete()
                .addOnSuccessListener {
                    FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document(user.uid)
                        .collection("participation")
                        .document(event.uid)
                        .delete()
                }
        }

        fun checkUserParticipation(
            user: User,
            event: Event,
            onTrue: () -> Unit,
            onFalse: () -> Unit
        ) {
            FirebaseFirestore
                .getInstance()
                .collection("events")
                .document(event.uid)
                .collection("attendees")
                .get()
                .addOnSuccessListener { query ->
                    var found = false

                    query.documents.forEach { doc ->
                        if (doc.id == user.uid) found = true
                    }

                    if (found) onTrue() else onFalse()
                }
        }

        private fun getEventRequest(uid: String): Task<DocumentSnapshot> {
            return FirebaseFirestore
                .getInstance()
                .collection("requests")
                .document(uid)
                .get()
        }

        private fun getEvent(uid: String): Task<DocumentSnapshot> {
            return FirebaseFirestore
                .getInstance()
                .collection("events")
                .document(uid)
                .get()
        }

        fun subscribeToMyRequests(
            user: User,
            onAdded: (DocumentSnapshot) -> Unit,
            onRemoved: (DocumentSnapshot) -> Unit
        ): ListenerRegistration {
            return FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.uid)
                .collection("requests")
                .addSnapshotListener { snapshot, error ->
                    if (snapshot !== null && error === null) {
                        for (dc in snapshot.documentChanges) {

                            if (dc.type == DocumentChange.Type.ADDED) {
                                getEventRequest(dc.document.id)
                                    .addOnSuccessListener {
                                        onAdded(it)
                                    }
                            } else if (dc.type == DocumentChange.Type.REMOVED) {
                                onRemoved(dc.document)
                            }

                        }
                    }
                }
        }

        fun subscribeToMyParticipation(
            user: User,
            onAdded: (DocumentSnapshot) -> Unit,
            onRemoved: (DocumentSnapshot) -> Unit
        ): ListenerRegistration {
            return FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.uid)
                .collection("participation")
                .addSnapshotListener { snapshot, error ->
                    if (snapshot !== null && error === null) {
                        for (dc in snapshot.documentChanges) {

                            if (dc.type == DocumentChange.Type.ADDED) {
                                getEvent(dc.document.id).addOnSuccessListener {
                                    onAdded(it)
                                }
                            } else if (dc.type == DocumentChange.Type.REMOVED) {
                                onRemoved(dc.document)
                            }

                        }
                    }
                }
        }

    }


}