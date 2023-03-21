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
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.com.Gameinstance.*;
import com.tools.stools;
import com.badlogic.gdx.math.Vector3;
public class xiangqitest implements ApplicationListener {
    private Vector3 vector3Temp1;
    private Vector3 vector3Temp2;private Vector3 vector3Temp3;private Vector3 vector3Temp4;
   //上一帧结束到现在经过的时间
    private chess chess1;
    private float chessheight;
    private float deltaTime;
    //判断是否正在加载
    public boolean loading;
    public AssetManager assets;
    private ModelBatch modelBatch;
    private Environment environment;
    private PerspectiveCamera cam;
    private ModelLoader loader;
    private Model model;
    private ModelInstance instance;
    private cameraInputTest camController;
    //模型集合,所有模型都在这,进行统一的模型资源管理
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public ModelInstance space;
    private int step;
    private Vector3 direction;
    private boolean key0;
    private Array<chess> chessArray;
    private float chessy=0;
    private int circle=0;
    @Override
    public void create () {

        vector3Temp1=new Vector3();
        vector3Temp2=new Vector3();
        vector3Temp3=new Vector3();
        vector3Temp4=new Vector3();
        chessArray=new Array<chess>();
        key0=false;
        chess1 =new chess();
        deltaTime=0;
        direction=new Vector3(-3,0,-3);
        step=0;
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        Gdx.graphics.setWindowedMode(800,600);
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(7f, 7f, 100f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        assets = new AssetManager();
        assets.load(stools.assetaddress("source/ChineseChess.g3db"), Model.class);
        loading = true;

        camController = new cameraInputTest(cam);
        //Gdx.input.setInputProcessor(camController);
    }

    @Override
    public void resize(int width, int height) {

    }

    //完成加载fbx-conv -f -v qipan.fbx qipan.g3db
    private void doneLoading() {
        //
        String id;
        Model model = assets.get(stools.assetaddress("source/ChineseChess.g3db"), Model.class);
        for (Node node1:model.nodes) {


            id = node1.id;
            ModelInstance instance = new ModelInstance(model, id);
            Node node = instance.getNode(id);
            instance.transform.set(node.globalTransform);
            node.translation.set(0,0,0);//重置坐标
            node.scale.set(1,1,1);//重置放缩比例
            node.rotation.idt();//重置方向
            instance.calculateTransforms();
            if(id.equals("QiPan"))
            {
                space=instance;
            }
            else
            {
                instances.add(instance);
                chessArray.add(new chess(instance));
            }
        }
        Vector3 location = chessArray.first().GetLocation();
        chessheight=location.y;
        //System.out.println(location.x+" ; "+location.y+" ; "+location.z);
        //qizi.first().
        //qizi.first().transform.setToTranslation(location.x,location.y,location.z);
        loading = false;
    }

    @Override
    public void render() {
        circle++;
        if(circle>=60&&!loading)
        {
            circle=0;
            Gdx.graphics.setTitle(Float.toString( chessArray.first().GetLocation().y));
        }

        if (loading && assets.update())
            doneLoading();
        deltaTime=Gdx.graphics.getDeltaTime();
        if(!Gdx.input.isButtonPressed(0)){
            if ((!key0) && Gdx.input.isTouched())
                camController.touchDown(Gdx.input.getX(), Gdx.input.getY(), 0, 0);

            if (key0 && Gdx.input.isTouched())
                camController.touchDragged(Gdx.input.getX(), Gdx.input.getY(), 0);

            if (key0 && !Gdx.input.isTouched())
                camController.touchUp(Gdx.input.getX(), Gdx.input.getY(), 0, 0);
        }
        else
        {
           chessArray.first().startPathAnimation();
        }
        for (chess chess1 : chessArray
             ) {
            chess1.runPathAnimation(deltaTime,false);
        }
        cam.update();
        key0=Gdx.input.isTouched();
        //Gdx.gl20.glEnable(GL20.GL_SAMPLE_ALPHA_TO_COVERAGE);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT|(Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        float deltaTime = Gdx.graphics.getDeltaTime();
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
