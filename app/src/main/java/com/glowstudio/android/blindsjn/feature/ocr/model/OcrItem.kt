package com.glowstudio.android.blindsjn.feature.ocr.model

/**
 * OCR로 추출된 상품 정보를 담는 데이터 클래스
 *
 * @property name 상품명
 * @property quantity 수량
 * @property price 가격
 */
data class OcrItem(
    val name: String,
    val quantity: Int,
    val price: Int
) 