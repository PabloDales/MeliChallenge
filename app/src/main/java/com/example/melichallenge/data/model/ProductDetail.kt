package com.example.melichallenge.data.model


import com.google.gson.annotations.SerializedName

data class ProductDetail(
    @SerializedName("id") val id: String? = null,
    @SerializedName("catalog_product_id") val catalogProductId: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("pdp_types") val pdpTypes: List<String>? = null,
    @SerializedName("domain_id") val domainId: String? = null,
    @SerializedName("permalink") val permalink: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("family_name") val familyName: String? = null,
    @SerializedName("type") val type: String? = null,
    // @SerializedName("buy_box_winner") // val buyBoxWinner: BuyBoxWinner? = null,
    @SerializedName("pickers") val pickers: Any? = null,
    @SerializedName("pictures") val pictures: List<Picture>? = null,
    @SerializedName("description_pictures") val descriptionPictures: List<Any>? = null,
    // @SerializedName("main_features") // val mainFeatures: List<MainFeature>? = null,
    @SerializedName("disclaimers") val disclaimers: List<Any>? = null,
    @SerializedName("attributes") val attributes: List<Attribute>? = null,
    // @SerializedName("short_description")
    // val shortDescription: ShortDescription? = null,
    @SerializedName("parent_id") val parentId: String? = null,
    @SerializedName("user_product") val userProduct: Any? = null,
    @SerializedName("children_ids") val childrenIds: List<Any>? = null,
    @SerializedName("settings") val settings: Settings? = null,
    @SerializedName("quality_type") val qualityType: String? = null,
    @SerializedName("release_info") val releaseInfo: Any? = null,
    @SerializedName("presale_info") val presaleInfo: Any? = null,
    @SerializedName("enhanced_content") val enhancedContent: Any? = null,
    @SerializedName("tags") val tags: List<Any>? = null,
    @SerializedName("date_created") val dateCreated: String? = null,
    @SerializedName("authorized_stores") val authorizedStores: Any? = null,
    @SerializedName("last_updated") val lastUpdated: String? = null,
    @SerializedName("grouper_id") val grouperId: Any? = null,
    @SerializedName("experiments") val experiments: Map<String, Any>? = null
)