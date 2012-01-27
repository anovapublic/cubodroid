/**
 * Anova IT Consulting 2011
 *
 * This file is licensed under the GPL version 3.
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package es.anovagroup.moviles.modulo4.cubodroid.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Date;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;

public final class CubeRenderer implements Renderer {
	private final IntBuffer vertexBuffer;
	private final FloatBuffer textureBuffer;
	private ByteBuffer indexBuffer;
	private int[] textures;
	private final Bitmap image;

	private double angle;
	private long last = 0;

	public CubeRenderer(final Bitmap image) {
		this.image = image;
		
		final ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asIntBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		final ByteBuffer tbb = ByteBuffer.allocateDirect(
				texture_coord.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		textureBuffer = tbb.asFloatBuffer();
		textureBuffer.put(texture_coord);
		textureBuffer.position(0);
		
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	
	public void onDrawFrame(final GL10 gl) {
		gl.glClear(
				GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -3.0f);

		final long now = new Date().getTime();
		
		if (last != 0) {
			double elapsed =  now - last;
			angle += (90 * elapsed) / 1000.0;
			
			gl.glRotatef((float) angle, 1, 0, 0);
			gl.glRotatef((float) angle, 0, 1, 0);
			gl.glRotatef((float) angle, 0, 1, 0);
		}

		last = now;
		drawCube(gl);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {
		gl.glDisable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_CULL_FACE);

		initTexture(gl);
	}
	
	private void drawCube(final GL10 gl) {
		gl.glFrontFace(GL10.GL_CCW);
		gl.glCullFace(GL10.GL_BACK);
		
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);

		gl.glDrawElements(
				GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, indexBuffer);
	}

	private void initTexture(final GL10 gl) {
		textures = new int[1];
		gl.glGenTextures(1, textures, 0);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		//GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, image, 0);
		gl.glActiveTexture(GL10.GL_TEXTURE0);
	}

	private final int one = 0x8000;
	private final int vertices[] = {
			-one, -one, -one,
			 one, -one, -one,
			 one,  one, -one,
			-one,  one, -one,
			-one, -one,  one,
			 one, -one,  one,
			 one,  one,  one,
			-one,  one,  one
	};

	private final float texture_coord[] = {
			0.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			1.0f, 0.0f
	};

	private final byte indices[] = {
			0, 4, 5,    0, 5, 1,
			1, 5, 6,    1, 6, 2,
			2, 6, 7,    2, 7, 3,
			3, 7, 4,    3, 4, 0,
			4, 7, 6,    4, 6, 5,
			3, 0, 1,    3, 1, 2
	};
}
