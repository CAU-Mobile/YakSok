package com.example.yaksok.query

import android.annotation.SuppressLint
import androidx.room.util.copy
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.toObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

data class Appointment(
    val name: String = "",
    val details: String="",
    val geoPoint: GeoPoint = GeoPoint(0.0, 0.0),
    val time: Timestamp = Timestamp.now(),
    val memberIds: List<String> = emptyList(),
    val documentId: String? = null
)

class AppointmentQuery {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private val db = FirebaseFirestore.getInstance()
        private val appointmentsCollection = db.collection("appointments")

        fun createAppointment(
            name: String,
            details: String,
            geoPoint: GeoPoint,
            time: Timestamp,
            memberIds: List<String>,
            callBack: (Boolean, String?, String?) -> Unit,
        ) {
            val appointment = Appointment(name, details, geoPoint, time, memberIds)

            appointmentsCollection.add(appointment)
                .addOnSuccessListener { documentReference ->
                    val documentId = documentReference.id
                    if (documentId != null) {
                        val updatedAppointment = appointment.copy(documentId = documentId)
                        documentReference.set(updatedAppointment)
                            .addOnSuccessListener {
                                callBack(true, updatedAppointment.toString(), "Create Success!")
                            }
                            .addOnFailureListener { exception ->
                                callBack(false, null, "Failed to update document: ${exception.message}")
                            }
                    } else {
                        callBack(false, null, "Failed to get document ID.")
                    }
                }
                .addOnFailureListener {
                    callBack(false, null, it.toString())
                }
        }

        fun getAppointment(
            appointmentId: String,
            callBack: (Boolean, Appointment?, String?) -> Unit
        ) {
            appointmentsCollection.document(appointmentId).get()
                .addOnSuccessListener {
                    callBack(true, it.toObject<Appointment>(), "Get Success!")
                }
                .addOnFailureListener {
                    callBack(false, null, it.toString())
                }
        }

        fun getAppointmentsWithUserId(
            memberId: String,
            callBack: (Boolean, Map<String, Appointment>?, String?) -> Unit
        ) {
            appointmentsCollection.whereArrayContains("memberIds", memberId).get()
                .addOnSuccessListener { appointments ->
                    if (appointments.size() == 0) {
                        callBack(false, null, "No Appointments!")
                    } else {
                        val appointmentsList = HashMap<String, Appointment>()
                        appointments.forEach { appointmentsList[it.id] = it.toObject<Appointment>() }
                        callBack(true, appointmentsList.toMap(), "Get Success!")
                    }
                }
                .addOnFailureListener {
                    callBack(false, null, it.toString())
                }
        }

        fun getAppointmentsAddname(
            memberId: String,
            callBack: (Boolean, Map<String, Appointment>?, String?) -> Unit
        ) {
            appointmentsCollection.whereArrayContains("memberIds", memberId).get()
                .addOnSuccessListener { appointments ->
                    if (appointments.size() == 0) {
                        callBack(false, null, "No Appointments!")
                    } else {
                        val appointmentsList = HashMap<String, Appointment>()
                        appointments.forEach { appointmentsList[it.id] = it.toObject<Appointment>() }
                        callBack(true, appointmentsList.toMap(), "Get Success!")
                    }
                }
                .addOnFailureListener {
                    callBack(false, null, it.toString())
                }
        }

        fun deleteAppointment(
            appointmentId: String,
            callBack: (Boolean, String?) -> Unit
        ) {
            appointmentsCollection.document(appointmentId).delete()
                .addOnSuccessListener {
                    callBack(true, "Delete Success!")
                }
                .addOnFailureListener {
                    callBack(false, it.toString())
                }
        }

        fun updateAppointment(
            appointmentId: String,
            name: String? = null,
            geoPoint: GeoPoint? = null,
            time: Timestamp? = null,
            memberIds: List<String>? = null,
            callBack: (Boolean, String?) -> Unit
        ) {
            appointmentsCollection.document(appointmentId).get()
                .addOnSuccessListener {
                    val originAppointment = it.toObject<Appointment>()
                    appointmentsCollection.document(appointmentId).update(
                        "name", name ?: originAppointment?.name,
                        "geoPoint", geoPoint ?: originAppointment?.geoPoint,
                        "time", time ?: originAppointment?.time,
                        "userIds", memberIds ?: originAppointment?.memberIds
                    )
                        .addOnSuccessListener {
                            callBack(true, "Update Success!")
                        }
                        .addOnFailureListener { e ->
                            callBack(false, e.toString())
                        }
                }
                .addOnFailureListener {
                    callBack(false, it.toString())
                }
        }

        fun addAppointmentMember(
            appointmentId: String,
            memberId: String,
            callBack: (Boolean, String?) -> Unit
        ) {
            appointmentsCollection.document(appointmentId).get()
                .addOnSuccessListener {
                    val originAppointment = it.toObject<Appointment>()
                    val memberIds = originAppointment?.memberIds?.toMutableList()
                    memberIds?.add(memberId)

                    appointmentsCollection.document(appointmentId).update("userIds", memberIds)
                        .addOnSuccessListener {
                            callBack(true, "Add Success!")
                        }
                        .addOnFailureListener { e ->
                            callBack(false, e.toString())
                        }
                }
                .addOnFailureListener {
                    callBack(false, it.toString())
                }
        }

        fun deleteAppointmentMember(
            appointmentId: String,
            memberId: String,
            callBack: (Boolean, String?) -> Unit
        ) {
            appointmentsCollection.document(appointmentId).get()
                .addOnSuccessListener {
                    val originAppointment = it.toObject<Appointment>()
                    val memberIds = originAppointment?.memberIds?.toMutableList()
                    if (memberIds?.remove(memberId) == true) {
                        appointmentsCollection.document(appointmentId).update("userIds", memberIds)
                            .addOnSuccessListener {
                                callBack(true, "Delete Success!")
                            }
                            .addOnFailureListener { e ->
                                callBack(false, e.toString())
                            }
                    } else {
                        callBack(false, "Cannot find member Id!")
                    }
                }
                .addOnFailureListener {
                    callBack(false, it.toString())
                }
        }
    }
}

class AppointmentQueryCoroutine {
    companion object {
        suspend fun createAppointment(
            name: String,
            details: String,
            geoPoint: GeoPoint,
            time: Timestamp,
            memberIds: List<String>
        ): String? {
            return suspendCoroutine { continuation ->
                AppointmentQuery.createAppointment(
                    name, details, geoPoint, time, memberIds
                ) { isSuccess, appointmentId, log ->
                        if (isSuccess) {
                            continuation.resume(appointmentId)
                        } else {
                            continuation.resumeWithException(Exception(log))
                        }
                }
            }
        }

        suspend fun getAppointment(
            appointmentId: String
        ): Appointment? {
            return suspendCoroutine { continuation ->
                AppointmentQuery.getAppointment(
                    appointmentId
                ) { isSuccess, appointment, log ->
                        if (isSuccess) {
                            continuation.resume(appointment)
                        } else {
                            continuation.resumeWithException(Exception(log))
                        }
                }
            }
        }

        suspend fun getAppointmentsWithUserId(
            memberId: String
        ): Map<String, Appointment>? {
            return suspendCoroutine { continuation ->
                AppointmentQuery.getAppointmentsWithUserId(
                    memberId
                ) { isSuccess, resultMap, log ->
                        if (isSuccess) {
                            continuation.resume(resultMap)
                        } else {
                            continuation.resumeWithException(Exception(log))
                        }
                }
            }
        }
        suspend fun getAppointmentId(
            appointment: Appointment
        ): Unit{
            return suspendCoroutine { continuation ->
            }
        }

        suspend fun deleteAppointment(
            appointmentId: String
        ): Unit {
            return suspendCoroutine { continuation ->
                AppointmentQuery.deleteAppointment(
                    appointmentId
                ) { isSuccess, log ->
                        if (isSuccess) {
                            continuation.resume(Unit)
                        } else {
                            continuation.resumeWithException(Exception(log))
                        }
                }
            }
        }

        suspend fun updateAppointment(
            appointmentId: String,
            name: String? = null,
            geoPoint: GeoPoint? = null,
            time: Timestamp? = null,
            memberIds: List<String>? = null
        ): Unit {
            return suspendCoroutine { continuation ->
                AppointmentQuery.updateAppointment(
                    appointmentId, name, geoPoint, time, memberIds
                ) { isSuccess, log ->
                    if (isSuccess) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }

        suspend fun addAppointmentMember(
            appointmentId: String,
            memberId: String
        ): Unit {
            return suspendCoroutine { continuation ->
                AppointmentQuery.addAppointmentMember(
                    appointmentId, memberId
                ) { isSuccess, log ->
                    if (isSuccess) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }

        suspend fun deleteAppointmentMember(
            appointmentId: String,
            memberId: String
        ): Unit {
            return suspendCoroutine { continuation ->
                AppointmentQuery.deleteAppointmentMember(
                    appointmentId, memberId
                ) { isSuccess, log ->
                    if (isSuccess) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }
    }
}