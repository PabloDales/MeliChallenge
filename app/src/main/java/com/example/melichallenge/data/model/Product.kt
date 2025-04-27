package com.example.melichallenge.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id: String,
    @SerializedName("site_id") val siteId: String,
    @SerializedName("name") val name: String,
    @SerializedName("date_created") val dateCreated: String? = null,
    @SerializedName("catalog_product_id") val catalogProductId: String? = null,
    @SerializedName("pdp_types") val pdpTypes: List<String>? = emptyList(),
    @SerializedName("status") val status: String? = null,
    @SerializedName("domain_id") val domainId: String? = null,
    @SerializedName("settings") val settings: Settings? = null,
    @SerializedName("main_features") val mainFeatures: List<Any>? = emptyList(),
    @SerializedName("attributes") val attributes: List<Attribute>? = null,
    @SerializedName("pictures") val pictures: List<Picture>? = null,
    @SerializedName("parent_id") val parentId: String? = null,
    @SerializedName("children_ids") val childrenIds: List<String>? = emptyList(),
    @SerializedName("quality_type") val qualityType: String? = null,
    @SerializedName("priority") val priority: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("variations") val variations: List<Any>? = emptyList(),
    @SerializedName("keywords") val keywords: String? = null,
    @SerializedName("description") val description: String? = null
)
