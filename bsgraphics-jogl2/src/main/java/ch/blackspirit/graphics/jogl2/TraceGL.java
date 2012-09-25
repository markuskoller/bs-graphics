package ch.blackspirit.graphics.jogl2;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.opengl.GL2;
import javax.media.opengl.TraceGL2;

final class TraceGL extends TraceGL2 {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private StringBuffer printed = new StringBuffer();
	private Level level;
	
	public TraceGL(GL2 downstreamGL, Level level) {
		super(downstreamGL, System.out);
		this.level = level;
	}

	@Override
	protected void print(String str) {
		printed.append(str);
	}

	@Override
	protected void printIndent() {}

	@Override
	protected void println(String str) {
		printed.append(str);
		log();
	}
	
	private void log() {
		LOGGER.log(level, printed.toString());
		printed.delete(0, printed.length());
	}
}
