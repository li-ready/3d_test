package com.example;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;

public class BehindTheScenesTest2 implements ApplicationListener {
    private String data;
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    //依然是管理所有需要渲染的渲染管理器
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;
    public boolean loading;
    //两个专门设置了管理器可能是为了后续开发
    public Array<ModelInstance> blocks = new Array<ModelInstance>();
    public Array<ModelInstance> invaders = new Array<ModelInstance>();
    public ModelInstance ship;
    public ModelInstance space;
    public Model model;
    public Renderable renderable;
    @Override
    public void create () {
        data ="assets/behindscenes/data";
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 7f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        ModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        ModelData modelData = modelLoader.loadModelData(Gdx.files.internal(data+"/invaderscene.g3dj"));
        model = new Model(modelData, new TextureProvider.FileTextureProvider());

        NodePart blockPart = model.getNode("ship").parts.get(0);

        renderable = new Renderable();
        renderable.meshPart.set(blockPart.meshPart);
        renderable.material = blockPart.material;
        renderable.environment = environment;
        renderable.worldTransform.idt();
        loading = true;
    }
//stools.assetaddress("loadscene/spacesphere.obj")

    private void doneLoading() {
        //在模型还是整体时,改变"bloack_default1"这一所有方块都共用的材质属性中的漫反射颜色,所有的"方块"都会被改变
        // 在后来的将所有模型节点独立出去后,"方块"们依然会集成同样的材质性质
       /* Material blockMaterial = model.getMaterial("block_default1");
        blockMaterial.set(ColorAttribute.createDiffuse(Color.YELLOW));*/
        for (int i = 0; i < model.nodes.size; i++) {
            String id = model.nodes.get(i).id;
            ModelInstance instance = new ModelInstance(model, id);
            Node node = instance.getNode(id);
            //globalTransform是<节点>在整个模型中的初始位置
            instance.transform.set(node.globalTransform);
            node.translation.set(0,0,0);
            node.scale.set(1,1,1);
            node.rotation.idt();
            instance.calculateTransforms();

            if (id.equals("space")) {
                space = instance;
                continue;
            }

            instances.add(instance);

            if (id.equals("ship"))
                ship = instance;
            else if (id.startsWith("block"))
                blocks.add(instance);
            else if (id.startsWith("invader"))
                invaders.add(instance);
        }
        //在所有的节点都独立出去后,所有的"方块"都有一份独立的材质属性,这时候我们就能遍历单独更改他们的材质属性了
        for (ModelInstance block : blocks) {
            float r = 0.5f + 0.5f * (float)Math.random();
            float g = 0.5f + 0.5f * (float)Math.random();
            float b = 0.5f + 0.5f * (float)Math.random();
            block.materials.get(0).set(ColorAttribute.createDiffuse(r, g, b, 1));
        }
        loading = false;
    }

    @Override
    public void render () {
        if (loading)
            doneLoading();
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(renderable);
        modelBatch.end();
    }

    @Override
    public void dispose () {
        modelBatch.dispose();
        instances.clear();
    }

    @Override
    public void resume () {
    }

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void pause () {
    }
}