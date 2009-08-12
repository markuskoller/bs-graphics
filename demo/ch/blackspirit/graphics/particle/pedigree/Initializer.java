/*
 * Copyright 2008-2009 Markus Koller
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.blackspirit.graphics.particle.pedigree;

import java.util.Random;

import javax.vecmath.Color4f;

import ch.blackspirit.graphics.geometry.Transformations;
import ch.blackspirit.graphics.particle.Particle;

/*
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<system additive="true" points="false">
	<emitter imageName="" name="Default" renderType="inherit" useAdditive="false" useOriented="false">
		<spawnInterval enabled="false" max="100.0" min="100.0"/>
		<spawnCount enabled="false" max="5.0" min="5.0"/>
		<emitCount enabled="true" max="1000.0" min="1000.0"/>
		<initialLife enabled="false" max="1000.0" min="1000.0"/>
		<initialSize enabled="false" max="10.0" min="10.0"/>
		<xOffset enabled="true" max="2.0" min="1.0"/>
		<yOffset enabled="true" max="2.0" min="1.0"/>
		<initialDistance enabled="true" max="2.0" min="1.0"/>
		<speed enabled="false" max="50.0" min="50.0"/>
		<length enabled="true" max="1000.0" min="1000.0"/>
		<spread type="random" value="334.0"/>
		<angularOffset type="simple" value="17.0"/>
		<growthFactor type="simple" value="5.0"/>
		<gravityFactor type="simple" value="3.0"/>
		<windFactor type="simple" value="5.0"/>
		<startAlpha type="simple" value="235.0"/>
		<endAlpha type="simple" value="24.0"/>
		<alpha active="false" max="255" min="0" type="linear">
			<point x="0.0" y="0.0"/>
			<point x="1.0" y="255.0"/>
		</alpha>
		<size active="false" max="255" min="0" type="linear">
			<point x="0.0" y="0.0"/>
			<point x="1.0" y="255.0"/>
		</size>
		<velocity active="false" max="1" min="0" type="linear">
			<point x="0.0" y="0.0"/>
			<point x="1.0" y="1.0"/>
		</velocity>
		<scaleY active="false" max="1" min="0" type="linear">
			<point x="0.0" y="0.0"/>
			<point x="1.0" y="1.0"/>
		</scaleY>
		<color>
			<step b="0.6" g="1.0" offset="0.0" r="1.0"/>
			<step b="0.0" g="0.0" offset="1.0" r="1.0"/>
		</color>
	</emitter>
</system>
*/
/**
 * @author Markus Koller
 */
public class Initializer<T extends Particle> implements ch.blackspirit.graphics.particle.Initializer<T> {
	private Random random = new Random();
	
	private long lifeMin;
	private long lifeMax;
	private float sizeMin;
	private float sizeMax;
	private float xOffsetMin;
	private float xOffsetMax;
	private float yOffsetMin;
	private float yOffsetMax;
	private float distanceMin;
	private float distanceMax;
	private float speedMin;
	private float speedMax;
	private float angleMin;
	private float angleMax;
		
//	<length enabled="true" max="1000.0" min="1000.0"/>
//	<alpha active="false" max="255" min="0" type="linear">
//		<point x="0.0" y="0.0"/>
//		<point x="1.0" y="255.0"/>
//	</alpha>
//	<size active="false" max="255" min="0" type="linear">
//		<point x="0.0" y="0.0"/>
//		<point x="1.0" y="255.0"/>
//	</size>
//	<velocity active="false" max="1" min="0" type="linear">
//		<point x="0.0" y="0.0"/>
//		<point x="1.0" y="1.0"/>
//	</velocity>
//	<scaleY active="false" max="1" min="0" type="linear">
//		<point x="0.0" y="0.0"/>
//		<point x="1.0" y="1.0"/>
//	</scaleY>
//	<color>
//	<step b="0.6" g="1.0" offset="0.0" r="1.0"/>
//	<step b="0.0" g="0.0" offset="1.0" r="1.0"/>
//</color>	
	private Color4f color = new Color4f(1,1,.6f,1);
	
	public void initialize(T particle) {
		// init lifetime
		particle.setInitialEnergy(lifeMin + (long)(random.nextFloat() * (float)(lifeMax - lifeMin)));
		particle.setEnergy(particle.getInitialEnergy());
		
		// init velocity
		particle.getVelocity().set(0, -(speedMin + (float)(random.nextFloat() * (float)(speedMax - speedMin))));
		float angle = angleMin + (float)(random.nextFloat() * (float)(angleMax - angleMin));
		Transformations.rotate(particle.getVelocity(), (float)Math.toRadians(angle));
		
		// init position
		particle.getPosition().x = 	distanceMin + (float)(random.nextFloat() * (float)(distanceMax - distanceMin));
		Transformations.rotate(particle.getPosition(), (float)(random.nextFloat() * Math.PI * 2));
		
		particle.getPosition().x +=	xOffsetMin + (float)(random.nextFloat() * (float)(xOffsetMax - xOffsetMin));
		particle.getPosition().y +=	yOffsetMin + (float)(random.nextFloat() * (float)(yOffsetMax - yOffsetMin));
		
		// init size
		float size = sizeMin + (float)(random.nextFloat() * (float)(sizeMax - sizeMin));
		particle.getSize().set(size, size);
		particle.getInitialSize().set(size, size);
		
		particle.getColor().set(color);
	}

	public long getLifeMin() {
		return lifeMin;
	}

	public void setLifeMin(long lifeMin) {
		this.lifeMin = lifeMin;
	}

	public long getLifeMax() {
		return lifeMax;
	}

	public void setLifeMax(long lifeMax) {
		this.lifeMax = lifeMax;
	}

	public float getSizeMin() {
		return sizeMin;
	}

	public void setSizeMin(float sizeMin) {
		this.sizeMin = sizeMin;
	}

	public float getSizeMax() {
		return sizeMax;
	}

	public void setSizeMax(float sizeMax) {
		this.sizeMax = sizeMax;
	}

	public float getXOffsetMin() {
		return xOffsetMin;
	}

	public void setXOffsetMin(float offsetMin) {
		xOffsetMin = offsetMin;
	}

	public float getXOffsetMax() {
		return xOffsetMax;
	}

	public void setXOffsetMax(float offsetMax) {
		xOffsetMax = offsetMax;
	}

	public float getYOffsetMin() {
		return yOffsetMin;
	}

	public void setYOffsetMin(float offsetMin) {
		yOffsetMin = offsetMin;
	}

	public float getYOffsetMax() {
		return yOffsetMax;
	}

	public void setYOffsetMax(float offsetMax) {
		yOffsetMax = offsetMax;
	}

	public float getSpeedMin() {
		return speedMin;
	}

	public void setSpeedMin(float speedMin) {
		this.speedMin = speedMin;
	}

	public float getSpeedMax() {
		return speedMax;
	}

	public void setSpeedMax(float speedMax) {
		this.speedMax = speedMax;
	}

	public float getAngleMin() {
		return angleMin;
	}

	public void setAngleMin(float angleMin) {
		this.angleMin = angleMin;
	}

	public float getAngleMax() {
		return angleMax;
	}

	public void setAngleMax(float angleMax) {
		this.angleMax = angleMax;
	}

	public Color4f getColor() {
		return color;
	}

	public void setColor(Color4f color) {
		this.color = color;
	}

	public float getDistanceMin() {
		return distanceMin;
	}

	public void setDistanceMin(float distanceMin) {
		this.distanceMin = distanceMin;
	}

	public float getDistanceMax() {
		return distanceMax;
	}

	public void setDistanceMax(float distanceMax) {
		this.distanceMax = distanceMax;
	}

}
