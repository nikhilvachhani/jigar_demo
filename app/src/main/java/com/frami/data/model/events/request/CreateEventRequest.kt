package com.frami.data.model.events.request

import com.frami.data.model.home.ActivityTypes
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateEventRequest(
    @field:SerializedName("EventId")
    var EventId: String = "",
    @field:SerializedName("UserId")
    var UserId: String = "",
    @field:SerializedName("userName")
    var userName: String = "",
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String? = null,
    @field:SerializedName("EventName")
    var EventName: String = "",
    @field:SerializedName("Description")
    var description: String = "",
    @field:SerializedName("Objective")
    var objective: String = "",
    @field:SerializedName("ActivityTypes")
    var activityTypes: List<ActivityTypes> = ArrayList(),
//    @field:SerializedName("ActivityType.Key")
//    var activityTypeKey: String = "",
//    @field:SerializedName("ActivityType.Name")
//    var activityTypeName: String = "",
//    @field:SerializedName("ActivityType.Icon")
//    var activityTypeIcon: String = "",
//    @field:SerializedName("ActivityType.Color")
//    var activityTypeColor: String = "",
//    @field:SerializedName("ActivityType.CombinationNo")
//    var activityTypeCombinationNo: Int,
    @field:SerializedName("Organizer.Id")
    var organizerId: String = "",
    @field:SerializedName("Organizer.Name")
    var organizerName: String = "",
    @field:SerializedName("Organizer.ImageUrl")
    var organizerImageUrl: String = "",
    @field:SerializedName("Organizer.OrganizerType")
    var organizerType: String = "",
    @field:SerializedName("Organizer.OrganizerPrivacy")
    var organizerPrivacy: String = "",
    @field:SerializedName("Eventtype")
    var Eventtype: String = "",
    @field:SerializedName("Venue.Name")
    var venueName: String = "",
    @field:SerializedName("Venue.Latitude")
    var venueLatitude: String = "",
    @field:SerializedName("Venue.Longitude")
    var venueLongitude: String = "",
    @field:SerializedName("LinkToPurchaseTickets")
    var linkToPurchaseTickets: String = "",
    @field:SerializedName("StartDate")
    var startDate: String = "",
    @field:SerializedName("EndDate")
    var endDate: String = "",
    @field:SerializedName("PrivacyType")
    var privacyType: String = "",
    @field:SerializedName("Invite")
    var invite: String = "",
    @field:SerializedName("Select")
    var select: String = "",
    @field:SerializedName("IsLimitedNumberOfParticipants")
    var isLimitedNumberOfParticipants: Boolean = false,
    @field:SerializedName("NumberOfParticipants")
    var numberOfParticipants: Int = 0,
) : Serializable {
    constructor(
        EventName: String,
        description: String,
        objective: String,
    ) : this(
        EventId = "",
        UserId = "",
        userName = "",
        profilePhotoUrl = "",
        EventName = EventName,
        description = description,
        objective = objective,
//        activityTypeKey = "",
//        activityTypeName = "",
//        activityTypeIcon = "",
//        activityTypeColor = "",
//        activityTypeCombinationNo = 0,
        organizerId = ""
    )

}