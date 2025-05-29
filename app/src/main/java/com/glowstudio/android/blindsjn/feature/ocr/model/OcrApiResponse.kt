package com.glowstudio.android.blindsjn.feature.ocr.model

data class OcrApiResponse(
    val version: String,
    val requestId: String,
    val timestamp: Long,
    val images: List<OcrImage>
)

data class OcrImage(
    val uid: String,
    val name: String,
    val inferResult: String,
    val message: String,
    val fields: List<OcrField>
)

data class OcrField(
    val valueType: String,
    val boundingPoly: BoundingPoly,
    val inferText: String,
    val inferConfidence: Double
)

data class BoundingPoly(
    val vertices: List<Vertex>
)

data class Vertex(
    val x: Int,
    val y: Int
)

/**
 * OCR API 응답을 OcrItem 리스트로 변환하는 확장 함수
 * 
 * @return OCR 결과에서 추출한 상품 정보 리스트
 */
fun OcrApiResponse.toOcrItems(): List<OcrItem> {
    return images.flatMap { image ->
        // 상품명, 수량, 가격 정보를 추출
        val items = mutableListOf<OcrItem>()
        var currentName = ""
        var currentQuantity = 1
        var currentPrice = 0
        
        image.fields.forEach { field ->
            when {
                // 상품명 추출 (예: "아메리카노")
                field.inferText.matches(Regex("^[가-힣a-zA-Z]+$")) -> {
                    if (currentName.isNotEmpty() && currentPrice > 0) {
                        items.add(OcrItem(currentName, currentQuantity, currentPrice))
                    }
                    currentName = field.inferText
                    currentQuantity = 1
                    currentPrice = 0
                }
                // 수량 추출 (예: "2개")
                field.inferText.matches(Regex("^\\d+개?$")) -> {
                    currentQuantity = field.inferText.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 1
                }
                // 가격 추출 (예: "4,000원")
                field.inferText.matches(Regex("^[0-9,]+원$")) -> {
                    currentPrice = field.inferText.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
                }
            }
        }
        
        // 마지막 아이템 추가
        if (currentName.isNotEmpty() && currentPrice > 0) {
            items.add(OcrItem(currentName, currentQuantity, currentPrice))
        }
        
        items
    }
} 