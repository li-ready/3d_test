package com.example;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.tools.stools;

public class xiangqitest implements ApplicationListener {
    //判断是否正在加载
    public boolean loading;
    public AssetManager assets;
    private ModelBatch modelBatch;
    private Environment environment;
    private PerspectiveCamera cam;
    private ModelLoader loader;
    private Model model;
    private ModelInstance instance;
    private CameraInputController camController;
    //模型集合,所有模型都在这,进行统一的模型资源管理
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public ModelInstance space;
    public Array<ModelInstance> qizi=new Array<ModelInstance>();
    @Override
    public void create () {

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(7f, 7f, 100f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        assets = new AssetManager();
        assets.load(stools.assetaddress("source/ChineseChess.g3db"), Model.class);
        loading = true;

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
    }

    @Override
    public void resize(int width, int height) {

    }
    //完成加载fbx-conv -f -v qipan.fbx qipan.g3db
    private void doneLoading() {
        //
        Model model = assets.get(stools.assetaddress("source/ChineseChess.g3db"), Model.class);
        for (int i = 0; i < model.nodes.size; i++) {
            String id = model.nodes.get(i).id;
            ModelInstance instance = new ModelInstance(model, id);
            Node node = instance.getNode(id);

            instance.transform.set(node.globalTransform);
            node.translation.set(0,0,0);
            node.scale.set(1,1,1);
            node.rotation.idt();
            instance.calculateTransforms();
            if(id.equals("QiPan"))
            {
                space=instance;
            }
            else
            {
                instances.add(instance);
                qizi.add(instance);
            }
//            if (id.equals("space")) {
//                space = instance;
//                continue;
//            }
//
//            instances.add(instance);
//
//            if (id.equals("ship"))
//                ship = instance;
//            else if (id.startsWith("block"))
//                blocks.add(instance);
//            else if (id.startsWith("invader"))
//                invaders.add(instance);
        }
        int i=0;
        for (ModelInstance qi:qizi) {
            i++;
            qi.transform.trn(0,2*(i/2),0);
        }
        //实例化36艘飞船,为什么用float当循环,因为要通过float来确定飞船位置
//        for (float x = -5f; x <= 5f; x += 2f) {
//            for (float z = -5f; z <= 5f; z += 2f) {
//                ModelInstance shipInstance = new ModelInstance(ship);
//                shipInstance.transform.setToTranslation(x, 0, z);
//                instances.add(shipInstance);
//            }
//        }
//        ModelInstance shipInstance = new ModelInstance(ship);
//        //把新的"飞船"实例添加进模型资源管理器
//        instances.add(shipInstance);
//        //完成了所有的模型加载,加载标志值0
        loading = false;
    }

    @Override
    public void render() {
        if (loading && assets.update())
            doneLoading();
        camController.update();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        //渲染instances集合,把所有模型都渲染
        modelBatch.render(instances, environment);
        if (space != null)
            modelBatch.render(space);
        modelBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

}