package com.example.melichallenge.data.model


object TestDataFactory {

    val sampleProducts = listOf(
        Product(
            id = "MLA1234567890",
            siteId = "MLA",
            name = "Smartphone Samsung Galaxy S23 Ultra",
            dateCreated = "2023-01-15T10:30:45.000Z",
            catalogProductId = "SAMGS23U256",
            pdpTypes = listOf("standard", "highlighted"),
            status = "active",
            domainId = "electronics",
            mainFeatures = emptyList(),
            attributes = listOf(
                Attribute(
                    id = "BRAND",
                    name = "Marca",
                    valueId = "SAMSUNG",
                    valueName = "Samsung"
                ),
                Attribute(
                    id = "MODEL",
                    name = "Modelo",
                    valueId = "GALAXY_S23_ULTRA",
                    valueName = "Galaxy S23 Ultra"
                ),
                Attribute(
                    id = "COLOR",
                    name = "Color",
                    valueId = "BLACK",
                    valueName = "Negro"
                )
            ),
            pictures = listOf(
                Picture(
                    id = "123456789",
                    url = "https://example.com/smartphone_1.jpg"
                ),
                Picture(
                    id = "987654321",
                    url = "https://example.com/smartphone_2.jpg"
                )
            ),
            parentId = null,
            childrenIds = emptyList(),
            qualityType = "premium",
            priority = "high",
            type = "item",
            variations = emptyList(),
            keywords = "smartphone, samsung, galaxy, s23, ultra, celular, móvil",
            description = "El Samsung Galaxy S23 Ultra representa lo mejor en tecnología móvil con su potente procesador, cámara de alta resolución y pantalla de alta definición."
        ),

        Product(
            id = "MLA9876543210",
            siteId = "MLA",
            name = "Notebook Apple MacBook Pro M2 Pro 14\"",
            dateCreated = "2023-02-20T14:25:30.000Z",
            catalogProductId = "APPMBP14M2P",
            pdpTypes = listOf("standard", "highlighted", "premium"),
            status = "active",
            domainId = "computers",
            mainFeatures = emptyList(),
            attributes = listOf(
                Attribute(
                    id = "BRAND",
                    name = "Marca",
                    valueId = "APPLE",
                    valueName = "Apple"
                ),
                Attribute(
                    id = "MODEL",
                    name = "Modelo",
                    valueId = "MACBOOK_PRO_14",
                    valueName = "MacBook Pro 14\""
                ),
                Attribute(
                    id = "PROCESSOR",
                    name = "Procesador",
                    valueId = "M2_PRO",
                    valueName = "M2 Pro"
                )
            ),
            pictures = listOf(
                Picture(
                    id = "111222333",
                    url = "https://example.com/macbook_1.jpg"
                )
            ),
            parentId = null,
            childrenIds = emptyList(),
            qualityType = "premium",
            priority = "highest",
            type = "item",
            variations = emptyList(),
            keywords = "notebook, laptop, apple, macbook, pro, m2, computadora, portátil",
            description = "La MacBook Pro con el chip M2 Pro lleva la potencia y la eficiencia a un nuevo nivel, con un rendimiento excepcional incluso para las tareas más exigentes."
        ),

        Product(
            id = "MLA5678901234",
            siteId = "MLA",
            name = "Auriculares Inalámbricos Sony WH-1000XM5",
            dateCreated = "2023-03-10T09:15:20.000Z",
            catalogProductId = "SONYWH1000XM5",
            pdpTypes = listOf("standard"),
            status = "active",
            domainId = "audio",
            mainFeatures = emptyList(),
            attributes = listOf(
                Attribute(
                    id = "BRAND",
                    name = "Marca",
                    valueId = "SONY",
                    valueName = "Sony"
                ),
                Attribute(
                    id = "MODEL",
                    name = "Modelo",
                    valueId = "WH_1000XM5",
                    valueName = "WH-1000XM5"
                ),
                Attribute(
                    id = "TYPE",
                    name = "Tipo",
                    valueId = "HEADPHONES",
                    valueName = "Auriculares de diadema"
                ),
                Attribute(
                    id = "CONNECTIVITY",
                    name = "Conectividad",
                    valueId = "WIRELESS",
                    valueName = "Inalámbrico"
                )
            ),
            pictures = listOf(
                Picture(
                    id = "444555666",
                    url = "https://example.com/headphones_1.jpg",
                )
            ),
            parentId = null,
            childrenIds = emptyList(),
            qualityType = "standard",
            priority = "medium",
            type = "item",
            variations = emptyList(),
            keywords = "auriculares, sony, bluetooth, wireless, inalambricos, noise cancelling",
            description = "Los auriculares Sony WH-1000XM5 ofrecen una experiencia de sonido premium con la mejor cancelación de ruido de su clase y una calidad de audio excepcional."
        )
    )

}