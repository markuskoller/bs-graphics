/*
 * Copyright (c) 2005 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 * 
 * Sun gratefully acknowledges that this software was originally authored
 * and developed by Kenneth Bradley Russell and Christopher John Kline.
 */

package ch.blackspirit.graphics.jogl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.spi.TextureProvider;

/**
 * Code copied with minimal changes to have it accessible as this originally 
 * was a protected static class: TextureIO.IIOTextureProvider 
 * @author Markus Koller
 */
public class ImageIOTextureProvider implements TextureProvider {
	public TextureData newTextureData(File file, int internalFormat,
			int pixelFormat, boolean mipmap, String fileSuffix)
			throws IOException {
		BufferedImage img = ImageIO.read(file);
		if (img == null) {
			return null;
		}
		return new TextureData(internalFormat, pixelFormat, mipmap, img);
	}

	public TextureData newTextureData(InputStream stream, int internalFormat,
			int pixelFormat, boolean mipmap, String fileSuffix)
			throws IOException {
		BufferedImage img = ImageIO.read(stream);
		if (img == null) {
			return null;
		}
		return new TextureData(internalFormat, pixelFormat, mipmap, img);
	}

	public TextureData newTextureData(URL url, int internalFormat,
			int pixelFormat, boolean mipmap, String fileSuffix)
			throws IOException {
		InputStream stream = url.openStream();
		try {
			return newTextureData(stream, internalFormat, pixelFormat, mipmap,
					fileSuffix);
		} finally {
			stream.close();
		}
	}
}
