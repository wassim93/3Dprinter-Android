package com.dmitrybrant.modelviewer;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dmitrybrant.modelviewer.stl.StlModel;
import com.wassimapp.a3dprinter.Configuration.ServiceConfig;
import com.wassimapp.a3dprinter.Models.Model3D;
import com.wassimapp.a3dprinter.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 * Copyright 2017 Dmitry Brant. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class MainActivityViewer extends AppCompatActivity {


    private ModelViewerApplication app;
    @Nullable private ModelSurfaceView modelView;
    private ViewGroup containerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainviewer);
        app = ModelViewerApplication.getInstance();

        containerView = (ViewGroup) findViewById(R.id.container_view);


        Intent i = getIntent();
        Model3D m = (Model3D) i.getSerializableExtra("obj");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(m.getImageName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Uri uri =  Uri.parse(ServiceConfig.FILE_DIR+m.getFileName());
        beginLoadModel(uri);



    }

    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        createNewModelView(app.getCurrentModel());

        if (app.getCurrentModel() != null) {
            setTitle(app.getCurrentModel().getTitle());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (modelView != null) {
            modelView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (modelView != null) {
            modelView.onResume();
        }
    }


    private void beginLoadModel(@NonNull Uri uri) {
        new ModelLoadTask().execute(uri);
    }

    private void createNewModelView(@Nullable Model model) {
        if (modelView != null) {
            containerView.removeView(modelView);
        }

        ModelViewerApplication.getInstance().setCurrentModel(model);
        modelView = new ModelSurfaceView(this, model);
        containerView.addView(modelView, 0);
    }

    private class ModelLoadTask extends AsyncTask<Uri, Integer, Model> {
        protected Model doInBackground(Uri... file) {
            InputStream stream = null;
            Model model = null;
            try {
                Uri uri = file[0];
                ContentResolver cr = getApplicationContext().getContentResolver();

                if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(uri.toString()).build();
                    Response response = client.newCall(request).execute();

                    // TODO: figure out how to NOT need to read the whole file at once.
                    stream = new ByteArrayInputStream(response.body().bytes());
                    // System.out.println("streaaam iff" + response.body().bytes());
                } else {
                    stream = cr.openInputStream(uri);

                }



                // assume it's STL.
                // TODO: autodetect file type by reading contents?
                model = new StlModel(stream);




            } catch (IOException e) {
                e.printStackTrace();
            }
            return model;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Model model) {
            if (isDestroyed()) {
                return;
            }
            if (model != null) {
                setCurrentModel(model);
            } else {
                Toast.makeText(getApplicationContext(), R.string.open_model_error, Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void setCurrentModel(@NonNull Model model) {
        createNewModelView(model);
        Toast.makeText(getApplicationContext(), R.string.open_model_success, Toast.LENGTH_SHORT).show();
        //setTitle(model.getTitle());
    }


}
