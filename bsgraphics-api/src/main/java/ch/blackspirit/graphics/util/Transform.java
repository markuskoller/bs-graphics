package ch.blackspirit.graphics.util;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector2f;

public final class Transform {
	private Transform() {}
	
	public static void setTranslation(Matrix3f matrix, float x, float y) {
		matrix.setIdentity();
		matrix.m02 = x;
		matrix.m12 = y;
	}
	public static void setRotationRad(Matrix3f matrix, float angleRad) {
		
	}
	public static void setRotationDeg(Matrix3f matrix, float angleDeg) {
		
	}
	public static void setScale(Matrix3f matrix, float x, float y) {
		
	}
	
	/**
	 * @param vector Vector to be transformed.
	 * @param matrix Transformation to apply.
	 */
	public static void transform(Vector2f vector, Matrix3f matrix) {
		
	}
	
	public static Matrix3f newMatrix() {
		Matrix3f matrix = new Matrix3f();
		matrix.setIdentity();
		return matrix;
	}
}
