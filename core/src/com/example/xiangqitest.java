package com.example;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.com.Gameinstance.*;
import com.com.itf.Shape;
import com.tools.stools;
import com.badlogic.gdx.math.Vector3;

public class xiangqitest extends InputAdapter implements ApplicationListener {
    private Vector3 vector3Temp1;
    private Vector3 vector3Temp2;private Vector3 vector3Temp3;private Vector3 vector3Temp4;
   //上一帧结束到现在经过的时间
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
    public ModelInstance space;
    private int step;
    private Vector3 direction;
    private boolean key0;
    private Array<chess> chessArray;
    private Array<chess> RedTakenChessArray;
    private Array<chess> BlackTakenChessArray;
    private float chessy=0;
    private int circle=0;
    private short[][] chessDistribution;
    private Array<Integer> reddeadchess;
    private Array<Integer> blackdeadchess;
    private Material selectionMaterial;
    private Material originalMaterial;
    private BoundingBox bounds;
    private Shape shape0;
    private int visibleCount;
    //以下是二维场景渲染4大件
    protected Stage stage;
    protected Label label;
    protected BitmapFont font;
    protected StringBuilder stringBuilder;
    private int roundControl=1;
    private int waitTaken;
    //回合的属性  -1:不是自己的回合  1:正在选棋  2:正在选步
    private int selecting=-1;//正在被选的棋子编号
    private int selected=-1;//已经被选的棋子编号
    private float waitTakenTime =0;

    @Override
    public void create () {
        RedTakenChessArray=new Array<chess>();
        BlackTakenChessArray=new Array<chess>();
        waitTaken=-1;
        selectionMaterial = new Material();
        originalMaterial=new Material();
        visibleCount = 0;
        stage = new Stage();
        font = new BitmapFont();
        label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
        stage.addActor(label);
        stringBuilder = new StringBuilder();
        bounds=new BoundingBox();
        chessDistribution=new short[9][11];
        vector3Temp1=new Vector3();
        vector3Temp2=new Vector3();
        vector3Temp3=new Vector3();
        vector3Temp4=new Vector3();
        chessArray=new Array<chess>();
        key0=false;
        deltaTime=0;
        direction=new Vector3(-3,0,-3);
        step=0;
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        Gdx.graphics.setWindowedMode(1200,900);
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
        Gdx.input.setInputProcessor(new InputMultiplexer(this, camController));
        //Gdx.input.setInputProcessor(camController);
    }

    @Override
    public void resize(int width, int height) {

    }
    private void refreshChessDistribution()
    {

    }
    //完成加载fbx-conv -f -v qipan.fbx qipan.g3db
    private void doneLoading() {
        //
        String id;
        Model model = assets.get(stools.assetaddress("source/ChineseChess.g3db"), Model.class);
        for (Node node1:model.nodes) {
            id = node1.id;
            chess chess0=new chess(model,id,true);
            if(id.equals("QiPan"))
            {
                space=chess0;
                continue;
            }
            else
            {
                chessArray.add(chess0);
                switch (id) {
                    case "R-che001":
                        chess0.setqipanLocation(-4,-5);chess0.setCamp(true);
                        break;
                    case "R-ma001":
                        chess0.setqipanLocation(-3,-5);chess0.setCamp(true);
                        break;
                    case "R-xiang001":
                        chess0.setqipanLocation(-2,-5);chess0.setCamp(true);
                        break;
                    case "R-shi001":
                        chess0.setqipanLocation(-1,-5);chess0.setCamp(true);
                        break;
                    case "R-jiang001":
                        chess0.setqipanLocation(0,-5);chess0.setCamp(true);
                        break;
                    case "R-shi002":
                        chess0.setqipanLocation(1,-5);chess0.setCamp(true);
                        break;
                    case "R-xiang002":
                        chess0.setqipanLocation(2,-5);chess0.setCamp(true);
                        break;
                    case "R-ma002":
                        chess0.setqipanLocation(3,-5);chess0.setCamp(true);
                        break;
                    case "R-che002":
                        chess0.setqipanLocation(4,-5);chess0.setCamp(true);
                        break;
                    case "R-bing001":
                        chess0.setqipanLocation(-4,-2);chess0.setCamp(true);
                        break;
                    case "R-bing002":
                        chess0.setqipanLocation(-2,-2);chess0.setCamp(true);
                        break;
                    case "R-bing003":
                        chess0.setqipanLocation(0,-2);chess0.setCamp(true);
                        break;
                    case "R-bing004":
                        chess0.setqipanLocation(2,-2);chess0.setCamp(true);
                        break;
                    case "R-bing005":
                        chess0.setqipanLocation(4,-2);chess0.setCamp(true);
                        break;
                    case "R-pao001":
                        chess0.setqipanLocation(-3,-3);chess0.setCamp(true);
                        break;
                    case "R-pao002":
                        chess0.setqipanLocation(3,-3);chess0.setCamp(true);
                        break;
                    case "B-che001":
                        chess0.setqipanLocation(-4,5);chess0.setCamp(false);
                        break;
                    case "B-ma001":
                        chess0.setqipanLocation(-3,5);chess0.setCamp(false);
                        break;
                    case "B-xiang001":
                        chess0.setqipanLocation(-2,5);chess0.setCamp(false);
                        break;
                    case "B-shi001":
                        chess0.setqipanLocation(-1,5);chess0.setCamp(false);
                        break;
                    case "B-jiang001":
                        chess0.setqipanLocation(0,5);chess0.setCamp(false);
                        break;
                    case "B-shi002":
                        chess0.setqipanLocation(1,5);chess0.setCamp(false);
                        break;
                    case "B-xiang002":
                        chess0.setqipanLocation(2,5);chess0.setCamp(false);
                        break;
                    case "B-ma002":
                        chess0.setqipanLocation(3,5);chess0.setCamp(false);
                        break;
                    case "B-che002":
                        chess0.setqipanLocation(4,5);chess0.setCamp(false);
                        break;
                    case "B-bing002":
                        chess0.setqipanLocation(-4,2);chess0.setCamp(false);
                        break;
                    case "B-bing003":
                        chess0.setqipanLocation(-2,2);chess0.setCamp(false);
                        break;
                    case "B-bing004":
                        chess0.setqipanLocation(0,2);chess0.setCamp(false);
                        break;
                    case "B-bing005":
                        chess0.setqipanLocation(2,2);chess0.setCamp(false);
                        break;
                    case "B-bing006":
                        chess0.setqipanLocation(4,2);chess0.setCamp(false);
                        break;
                    case "B-pao001":
                        chess0.setqipanLocation(-3,3);chess0.setCamp(false);
                        break;
                    case "B-pao002":
                        chess0.setqipanLocation(3,3);chess0.setCamp(false);
                        break;
                }
                chessArray.add(chess0);

            }
        }
        for (chess chess0:chessArray
             ) {
            chess0.qipanLocationRefresh();
/*
            System.out.println("chessX= "+chess0.GetLocation().x+"  chessY= "+chess0.GetLocation().y+"  chessZ= "+chess0.GetLocation().z);
*/
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
        visibleCount = 0;
        circle++;
        if (loading && assets.update())
            doneLoading();
        deltaTime=Gdx.graphics.getDeltaTime();
        if(waitTaken!=-1)
        {
            waitTakenTime-=deltaTime;
            if(waitTakenTime<=0f)
            {
               takeChessDone();
            }
        }
        for (chess chess1 : chessArray
             ) {
            chess1.runPathAnimation(deltaTime,false);
        }
        cam.update();
        key0=Gdx.input.isTouched();
        //Gdx.gl20.glEnable(GL20.GL_SAMPLE_ALPHA_TO_COVERAGE);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        modelBatch.begin(cam);
        //渲染instances集合,把所有模型都渲染
        for (final chess instance : chessArray) {
            if (instance.isVisible(cam)) {
                modelBatch.render(instance, environment);
                visibleCount++;
            }
        }
        for (final chess instance : BlackTakenChessArray) {
            if (instance.isVisible(cam)) {
                modelBatch.render(instance, environment);
                visibleCount++;
            }
        }
        for (final chess instance : BlackTakenChessArray) {
            if (instance.isVisible(cam)) {
                modelBatch.render(instance, environment);
                visibleCount++;
            }
        }

        if (space != null)
            modelBatch.render(space);
        modelBatch.end();
        stringBuilder.setLength(0);
        stringBuilder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
        stringBuilder.append(" Visible: ").append(visibleCount);
        stringBuilder.append(" Selected: ").append(roundControl);
        label.setText(stringBuilder);
        stage.draw();
    }
    public void takeChessDone(){
        waitTakenTime=0f;
        if(chessArray.get(waitTaken).isCamp())BlackTakenChessArray.add(chessArray.get(waitTaken));
        else RedTakenChessArray.add(chessArray.get(waitTaken));
        chessArray.get(waitTaken).jump(20,20);
        chessArray.removeIndex(waitTaken);
    }
    public void takeChess(chess chessO,int chessP)
    {
        if(waitTaken==-1){
            chessO.createPathAnimation(chessArray.get(chessP).getQipanx(),chessArray.get(chessP).getQipany());
            waitTaken=chessP;
            waitTakenTime =1f/chessO.getPathAnimationspeed();
        }
    }
    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        //if(roundControl>0)
        selecting = getObject(screenX, screenY);//选择一个棋子,没选上就返回-1
       // if(selected>=0)//选到了棋子
        {

        }
        return selecting >= 0;
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

    public synchronized void setSelected (int value) {
        if (selected == value) return;
        if (selected >= 0) {
            Material mat = chessArray.get(selected).materials.get(0);
            mat.clear();
            mat.set(originalMaterial);
        }
        selected = value;
        if (selected >= 0) {
            Material mat = chessArray.get(selected).materials.get(0);
            originalMaterial.clear();
            originalMaterial.set(mat);
            mat.clear();
            selectionMaterial.clear();
            selectionMaterial.set(originalMaterial);
            selectionMaterial.set(ColorAttribute.createDiffuse(Color.WHITE));
            mat.set(selectionMaterial);
        }
    }

    public int getObject (int screenX, int screenY) {
        Ray ray = cam.getPickRay(screenX, screenY);
        int result = -1;
        float distance = -1;
        for (int i = 0; i < chessArray.size; ++i) {
            final float dist2 = chessArray.get(i).intersects(ray);
            if (dist2 >= 0f && (distance < 0f || dist2 <= distance)) {
                result = i;
                distance = dist2;
            }
        }
        return result;
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
        chessArray.clear();
        assets.dispose();
    }

}
