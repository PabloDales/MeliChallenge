package com.example.melichallenge.data.model

import com.google.gson.annotations.SerializedName

data class Attribute(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("value_id") val valueId: String? = null,
    @SerializedName("value_name") val valueName: String? = null,
    @SerializedName("value_struct") val valueStruct: Any? = null,
    @SerializedName("values") val values: List<AttributeValue>? = null,
    @SerializedName("attribute_group_id") val attributeGroupId: String? = null,
    @SerializedName("attribute_group_name") val attributeGroupName: String? = null,
    @SerializedName("source") val source: Long? = null
)