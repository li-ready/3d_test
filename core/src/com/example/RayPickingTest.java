package com.example;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class RayPickingTest extends InputAdapter implements ApplicationListener {
    public static class GameObject extends ModelInstance {
        public final Vector3 center = new Vector3();
        public final Vector3 dimensions = new Vector3();
        public final float radius;

        private final static BoundingBox bounds = new BoundingBox();

        public GameObject (Model model, String rootNode, boolean mergeTransform) {
            super(model, rootNode, mergeTransform);
            calculateBoundingBox(bounds);
            bounds.getCenter(center);
            bounds.getDimensions(dimensions);
            radius = dimensions.len() / 2f;
        }
    }
    private String data;
    protected PerspectiveCamera cam;
    protected CameraInputController camController;
    protected ModelBatch modelBatch;
    protected AssetManager assets;
    protected Array<GameObject> instances = new Array<GameObject>();
    protected Environment environment;
    protected boolean loading;

    protected Array<GameObject> blocks = new Array<GameObject>();
    protected Array<GameObject> invaders = new Array<GameObject>();
    protected ModelInstance ship;
    protected ModelInstance space;

    protected Stage stage;
    protected Label label;
    protected BitmapFont font;
    protected StringBuilder stringBuilder;

    private int visibleCount;
    private Vector3 position = new Vector3();
    private int selected = -1, selecting = -1;
    private Material selectionMaterial;
    private Material originalMaterial;

    @Override
    public void create () {
        data="assets/loadscene/";
        stage = new Stage();
        font = new BitmapFont();
        label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
        stage.addActor(label);
        stringBuilder = new StringBuilder();

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 7f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(new InputMultiplexer(this, camController));

        assets = new AssetManager();
        assets.load(data + "invaderscene.g3db", Model.class);
        loading = true;

        selectionMaterial = new Material();
        selectionMaterial.set(ColorAttribute.createDiffuse(Color.ORANGE));
        originalMaterial = new Material();
    }

    private void doneLoading () {
        Model model = assets.get(data + "invaderscene.g3db", Model.class);
        for (int i = 0; i < model.nodes.size; i++) {
            String id = model.nodes.get(i).id;
            GameObject instance = new GameObject(model, id, true);

            if (id.equals("space")) {
                space = instance;
                continue;
            }

            instances.add(instance);

            if (id.equals("ship"))
                ship = instance;
            else if (id.startsWith("block"))
                blocks.add(instance);
            else if (id.startsWith("invader")) invaders.add(instance);
        }

        loading = false;
    }

    @Override
    public void render () {
        if (loading && assets.update()) doneLoading();
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        visibleCount = 0;
        for (final GameObject instance : instances) {
            if (isVisible(cam, instance)) {
                modelBatch.render(instance, environment);
                visibleCount++;
            }
        }
        if (space != null) modelBatch.render(space);
        modelBatch.end();

        stringBuilder.setLength(0);
        stringBuilder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
        stringBuilder.append(" Visible: ").append(visibleCount);
        stringBuilder.append(" Selected: ").append(selected);
        label.setText(stringBuilder);
        stage.draw();
    }

    protected boolean isVisible (final Camera cam, final GameObject instance) {
        instance.transform.getTranslation(position);
        position.add(instance.center);
        return cam.frustum.sphereInFrustum(position, instance.radius);
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        selecting = getObject(screenX, screenY);
        return selecting >= 0;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (selecting < 0)
            return false;
        if (selected == selecting) {
            Ray ray = cam.getPickRay(screenX, screenY);
            final float distance = -ray.origin.y / ray.direction.y;
            position.set(ray.direction).scl(distance).add(ray.origin);
            instances.get(selected).transform.setTranslation(position);
        }
        return true;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if (selecting >= 0) {
            if (selecting == getObject(screenX, screenY))
                setSelected(selecting);
            selecting = -1;
            return true;
        }
        return false;
    }

    public void setSelected (int value) {
        if (selected == value) return;
        if (selected >= 0) {
            Material mat = instances.get(selected).materials.get(0);
            mat.clear();
            mat.set(originalMaterial);
        }
        selected = value;
        if (selected >= 0) {
            Material mat = instances.get(selected).materials.get(0);
            originalMaterial.clear();
            originalMaterial.set(mat);
            mat.clear();
            mat.set(selectionMaterial);
        }
    }

    public int getObject (int screenX, int screenY) {
        Ray ray = cam.getPickRay(screenX, screenY);

        int result = -1;
        float distance = -1;

        for (int i = 0; i < instances.size; ++i) {
            final GameObject instance = instances.get(i);

            instance.transform.getTranslation(position);
            position.add(instance.center);

            final float len = ray.direction.dot(position.x-ray.origin.x, position.y-ray.origin.y, position.z-ray.origin.z);
            if (len < 0f)
                continue;

            float dist2 = position.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);
            if (distance >= 0f && dist2 > distance)
                continue;

            if (dist2 <= instance.radius * instance.radius) {
                result = i;
                distance = dist2;
            }
        }
        return result;
    }


    @Override
    public void dispose () {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

    @Override
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause () {
    }

    @Override
    public void resume () {
    }
}