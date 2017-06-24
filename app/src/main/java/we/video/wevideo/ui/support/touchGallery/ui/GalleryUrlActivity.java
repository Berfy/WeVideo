/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package we.video.wevideo.ui.support.touchGallery.ui;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;
import we.video.wevideo.R;
import we.video.wevideo.ui.support.touchGallery.adater.BasePagerAdapter;
import we.video.wevideo.ui.support.touchGallery.adater.UrlPagerAdapter;
import we.video.wevideo.ui.support.touchGallery.view.GalleryViewPager;

public class GalleryUrlActivity extends Activity {

	private GalleryViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// // 无title
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// // 全屏
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_touch_gallery_layout);
		initView();
	}

	private void initView() {
		ArrayList<String> bannerBeans = (ArrayList<String>) getIntent()
				.getSerializableExtra("data");
		if (null != bannerBeans) {
			int position = getIntent().getIntExtra("position", 0);
			UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this);
			pagerAdapter.getData().addAll(bannerBeans);
			pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
				@Override
				public void onItemChange(int currentPosition) {
				}
			});
			pagerAdapter.setOnItemClickListener(new BasePagerAdapter.OnItemClickListener() {

				@Override
				public void onItemClick(int currentPosition) {
					finish();
				}
			});
			mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
			mViewPager.setOffscreenPageLimit(3);
			mViewPager.setAdapter(pagerAdapter);
			mViewPager.setCurrentItem(position);
		}
	}

}