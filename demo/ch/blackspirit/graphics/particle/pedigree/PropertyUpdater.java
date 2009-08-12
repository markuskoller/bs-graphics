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

import ch.blackspirit.graphics.particle.LinearGradient;
import ch.blackspirit.graphics.particle.Particle;
import ch.blackspirit.graphics.particle.Updater;

/**
 * @author Markus Koller
 */
public class PropertyUpdater<T extends Particle> implements Updater<T> {
	private float growthFactor;
	
	private LinearGradient r = new LinearGradient();
	private LinearGradient g = new LinearGradient();
	private LinearGradient b = new LinearGradient();
	private LinearGradient a = new LinearGradient();
	
	public PropertyUpdater() {}

	public void update(T particle, long elapsedTime) {
		particle.setEnergy(particle.getEnergy() - elapsedTime);

		float lifetimePerc = (float)(particle.getInitialEnergy() - particle.getEnergy())  / (float)particle.getInitialEnergy();
		particle.getColor().x = r.getValue(lifetimePerc);
		particle.getColor().y = g.getValue(lifetimePerc);
		particle.getColor().z = b.getValue(lifetimePerc);
		particle.getColor().w = a.getValue(lifetimePerc);
		
		float scale = 1 + (lifetimePerc * (growthFactor - 1));
		particle.getSize().x = particle.getInitialSize().x * scale;
		particle.getSize().y = particle.getInitialSize().y * scale;
	}

	public float getGrowthFactor() {
		return growthFactor;
	}
	public void setGrowthFactor(float growthFactor) {
		this.growthFactor = growthFactor;
	}

	public LinearGradient getR() {
		return r;
	}
	public LinearGradient getG() {
		return g;
	}
	public LinearGradient getB() {
		return b;
	}
	public LinearGradient getA() {
		return a;
	}
}
