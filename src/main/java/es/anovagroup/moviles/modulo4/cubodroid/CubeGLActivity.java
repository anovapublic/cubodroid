/**
 * Anova IT Consulting 2011
 *
 * This file is licensed under the GPL version 3.
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package es.anovagroup.moviles.modulo4.cubodroid;

import es.anovagroup.moviles.modulo4.cubodroid.renderer.CubeRenderer;
import android.opengl.GLSurfaceView;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

public class CubeGLActivity extends Activity {

	private static String TAG = "cubodroid";

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 * If the activity is being re-initialized after previously being
	 * shut down then this Bundle contains the data it most recently
	 * supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
	 * is null.</b>
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		final GLSurfaceView view = new GLSurfaceView(this);

		Log.e("OpenGL ES 2.0", Boolean.toString(isOpenGLES20()));
		view.setRenderer(new CubeRenderer(loadBitmap()));
		setContentView(view);
	}

	private Bitmap loadBitmap() {
		return BitmapFactory.decodeResource(getResources(),
				R.drawable.anova);
	}
	private boolean isOpenGLES20() {
		final ActivityManager manager = 
				(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo info = manager.getDeviceConfigurationInfo();
		return info.reqGlEsVersion >= 0x20000;
	}
}
