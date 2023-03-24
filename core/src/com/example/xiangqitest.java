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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.com.Gameinstance.ChessKid.*;
import com.com.Gameinstance.cameraInputTest;
import com.com.Gameinstance.chess;
import com.com.itf.Shape;
import com.tools.stools;

public class xiangqitest extends InputAdapter implements ApplicationListener {
    private Vector2 vector2Temp;
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
    private int[][] chessFenbu;
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
    private int roundControl=-1;
    //回合的属性  -1:不是自己的回合  1:正在选棋  2:正在选步
    private int waitTaken;
    private int selecting=-1;//正在被选的棋子编号
    private int selected=-1;//已经被选的棋子编号
    private float waitTakenTime =0;
    private boolean RB=true;
    private Vector2[][] qipanDot;
    private float time;

    @Override
    public void create () {
        vector2Temp=new Vector2();
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
        chessFenbu =new int[9][11];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 11; j++) {
                chessFenbu[i][j]=0;//0代表空
            }
        }
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
        qipanDot=new Vector2[9][11];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 11; j++) {
                if(j==5) {
                    qipanDot[i][j]=new Vector2(0,0);
                    qipanDot[i][j].x = stools.qipanLocationToLocation(i - 4, j - 5, vector3Temp1).z;
                    qipanDot[i][j].y=0;
                    //System.out.println("dotx= "+i+"doty= "+j);
                }
                else {
                    //System.out.println("rightway dotx= "+i+"  doty= "+j);
                    //System.out.println("create  i= "+i+" j= "+j);//待删除
                    qipanDot[i][j] = new Vector2(0, 0);
                    qipanDot[i][j].x = stools.qipanLocationToLocation(i - 4, j - 5, vector3Temp1).z;
                    qipanDot[i][j].y = stools.qipanLocationToLocation(i - 4, j - 5, vector3Temp1).x;
                    //System.out.println("go x= "+qipanDot[i][j].x+"go y="+qipanDot[i][j].y);//待删除
                }
            }
        }
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
            ModelInstance model0=new ModelInstance(model,id,true);
            if(id.equals("QiPan"))
            {
                space=model0;
                continue;
            }
            else
            {
                chess chess0=new chess(model,id,true);
                switch (id) {
                    case "R-che001":
                        chess0=new Che(model,id,true);
                        chess0.setqipanLocation(-4,-5);
                        chess0.setCamp(true);
                        break;
                    case "R-ma001":
                        chess0=new Ma(model,id,true);
                        chess0.setqipanLocation(-3,-5);chess0.setCamp(true);
                        break;
                    case "R-xiang001":
                        chess0=new Xiang(model,id,true);
                        chess0.setqipanLocation(-2,-5);chess0.setCamp(true);
                        break;
                    case "R-shi001":
                        chess0=new Shi(model,id,true);
                        chess0.setqipanLocation(-1,-5);chess0.setCamp(true);
                        break;
                    case "R-jiang001":
                        chess0=new Jiang(model,id,true);
                        chess0.setqipanLocation(0,-5);chess0.setCamp(true);
                        break;
                    case "R-shi002":
                        chess0=new Shi(model,id,true);
                        chess0.setqipanLocation(1,-5);chess0.setCamp(true);
                        break;
                    case "R-xiang002":
                        chess0=new Xiang(model,id,true);
                        chess0.setqipanLocation(2,-5);chess0.setCamp(true);
                        break;
                    case "R-ma002":
                        chess0=new Ma(model,id,true);
                        chess0.setqipanLocation(3,-5);chess0.setCamp(true);
                        break;
                    case "R-che002":
                        chess0=new Che(model,id,true);
                        chess0.setqipanLocation(4,-5);chess0.setCamp(true);
                        break;
                    case "R-bing001":
                        chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(-4,-2);chess0.setCamp(true);
                        break;
                    case "R-bing002":
                        chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(-2,-2);chess0.setCamp(true);
                        break;
                    case "R-bing003":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(0,-2);chess0.setCamp(true);
                        break;
                    case "R-bing004":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(2,-2);chess0.setCamp(true);
                        break;
                    case "R-bing005":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(4,-2);chess0.setCamp(true);
                        break;
                    case "R-pao001":chess0=new Pao(model,id,true);
                        chess0.setqipanLocation(-3,-3);chess0.setCamp(true);
                        break;
                    case "R-pao002":chess0=new Pao(model,id,true);
                        chess0.setqipanLocation(3,-3);chess0.setCamp(true);
                        break;
                    case "B-che001":chess0=new Che(model,id,true);
                        chess0.setqipanLocation(-4,5);chess0.setCamp(false);
                        break;
                    case "B-ma001":chess0=new Ma(model,id,true);
                        chess0.setqipanLocation(-3,5);chess0.setCamp(false);
                        break;
                    case "B-xiang001":chess0=new Xiang(model,id,true);
                        chess0.setqipanLocation(-2,5);chess0.setCamp(false);
                        break;
                    case "B-shi001":chess0=new Shi(model,id,true);
                        chess0.setqipanLocation(-1,5);chess0.setCamp(false);
                        break;
                    case "B-jiang001":chess0=new Jiang(model,id,true);
                        chess0.setqipanLocation(0,5);chess0.setCamp(false);
                        break;
                    case "B-shi002":chess0=new Shi(model,id,true);
                        chess0.setqipanLocation(1,5);chess0.setCamp(false);
                        break;
                    case "B-xiang002":chess0=new Xiang(model,id,true);
                        chess0.setqipanLocation(2,5);chess0.setCamp(false);
                        break;
                    case "B-ma002":chess0=new Ma(model,id,true);
                        chess0.setqipanLocation(3,5);chess0.setCamp(false);
                        break;
                    case "B-che002":chess0=new Che(model,id,true);
                        chess0.setqipanLocation(4,5);chess0.setCamp(false);
                        break;
                    case "B-bing002":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(-4,2);chess0.setCamp(false);
                        break;
                    case "B-bing003":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(-2,2);chess0.setCamp(false);
                        break;
                    case "B-bing004":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(0,2);chess0.setCamp(false);
                        break;
                    case "B-bing005":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(2,2);chess0.setCamp(false);
                        break;
                    case "B-bing006":chess0=new Bing(model,id,true);
                        chess0.setqipanLocation(4,2);chess0.setCamp(false);
                        break;
                    case "B-pao001":chess0=new Pao(model,id,true);
                        chess0.setqipanLocation(-3,3);chess0.setCamp(false);
                        break;
                    case "B-pao002":chess0=new Pao(model,id,true);
                        chess0.setqipanLocation(3,3);chess0.setCamp(false);
                        break;
                }

                int x=chess0.getArrayQx();int y=chess0.getArrayQy();
                if(chess0.isCamp())
                chessFenbu[x][y]=1;
                else
                    chessFenbu[x][y]=-1;
                //System.out.println(id+" hh");//待删除
                if((id.equals("R-jiang001"))|(id.equals("B-jiang001"))) {
                    //System.out.println("hhhhhhhhhhhhhhh");//待删除
                    chessArray.insert(0, chess0);
                }
               else {
                    chessArray.add(chess0);
                }
            }
        }
        for (chess chess2:chessArray
             ) {
            chess2.qipanLocationRefresh();
/*
            System.out.println("chessX= "+chess0.GetLocation().x+"  chessY= "+chess0.GetLocation().y+"  chessZ= "+chess0.GetLocation().z);
*/
            //待删除
        }
        Vector3 location = chessArray.first().GetLocation();
        chessheight=location.y;
        System.out.println("chessheight = "+chessheight);//待删除
        //System.out.println(location.x+" ; "+location.y+" ; "+location.z);
        //qizi.first().
        //qizi.first().transform.setToTranslation(location.x,location.y,location.z);
        //待删除
       /* for (chess chess1 : chessArray) {

            System.out.println(chess1.nodes.get(0).id);
        }*/
        loading = false;
    }

    @Override
    public void render() {
        visibleCount = 0;
        time+=roundControl==-1?Gdx.graphics.getDeltaTime():0;
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
        if(time>=2f){
            if(roundControl==-1){
                System.out.println("you round");//待删除
                roundControl=1;
                RB=!RB;
            }
           time=0f;
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
        for (final chess instance : RedTakenChessArray) {
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
        System.out.println(waitTaken);
        if(chessArray.get(waitTaken).isCamp()){
            BlackTakenChessArray.add(chessArray.get(waitTaken));
            vector3Temp1= stools.qipanLocationToLocation(false,BlackTakenChessArray.size,vector3Temp1);
        }
        else{
            vector3Temp1= stools.qipanLocationToLocation(true,RedTakenChessArray.size,vector3Temp1);
            RedTakenChessArray.add(chessArray.get(waitTaken));
        }
        //输出吃棋特效,待删除,待更改
        System.out.println("takechess");
        chessFenbu[chessArray.get(waitTaken).getArrayQx()][chessArray.get(waitTaken).getArrayQy()]=0;
        chessArray.get(waitTaken).transform.setTranslation(vector3Temp1);
        chessArray.removeIndex(waitTaken);
        //System.out.println(RedTakenChessArray.get(0).nodes.get(0).id);
        waitTaken=-1;
    }
    public void takeChess(chess chessO,int chessP) {
        if(waitTaken==-1){
            chessO.createPathAnimation(chessArray.get(chessP).getQipanx(),chessArray.get(chessP).getQipany(), chessFenbu);
            waitTaken=chessP;
            waitTakenTime =1f/chessO.getPathAnimationspeed();
        }
    }
    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        //if(roundControl>0)
        if(roundControl!=-1) {
            selecting = getObject(screenX, screenY);//选择一个棋子,没选上就返回-1
            if (selected >= 0)//选到了棋子
            {

            }
        }
        return selecting >= 0;
    }
    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        boolean out = false;
        if(roundControl!=-1) {
            boolean isGetchess = false;
            boolean isGetDot = false;
            int i = 0;
            int j = 0;

            if (selecting >= 0) {
                if (selecting == getObject(screenX, screenY)) {
                    //???
                    out = true;
                    isGetchess = true;
                }
            }
            if (!out) {
                //选择棋盘上的一块区域
                Ray ray = cam.getPickRay(screenX, screenY);
                vector3Temp3 = (vector3Temp1.set(cam.position).add(vector3Temp2.set(ray.direction).setLength((cam.position.y - chessheight) / (ray.direction.dot(0, -1, 0) / ray.direction.len()))));
                //以上是棋盘表面上的3d坐标
                vector2Temp.x = vector3Temp3.z ;//- 32.194542f;
                vector2Temp.y = vector3Temp3.x ;//- 45.48245f;
                //待删除
                //System.out.println("chessX= " + chessArray.first().transform.getTranslation(vector3Temp1).z + "  chessY= " + chessArray.first().transform.getTranslation(vector3Temp1).x);
                System.out.println("rayX= " + vector2Temp.x + "  rayY= " + vector2Temp.y);
                boolean l = true;
                int cc = 0;
                for (int k = 0; k < 9; k++) {
                    if (vector2Temp.x > qipanDot[i][j].x) {
                        i++;
                        //System.out.println("i= "+i);
                    }
                }
                for (int k = 0; k < 11; k++) {
                    if (vector2Temp.y > qipanDot[0][j].y) {
                        j++;
                        //System.out.println("j= "+j);
                    }
                }
                //System.out.println("i= " + i + "  j= " + j);
                if (i > 0 && i < 9 && j > 0 && j < 11) {
                    float ds = 0;
                    if (!isGetDot&&vector2Temp.dst(qipanDot[i][j]) < 4f) {
                        isGetDot = j!=5;

                    }
                    if (!isGetDot&&vector2Temp.dst(qipanDot[i][j - 1]) < 4f) {
                        isGetDot = j!=6;
                        j-=isGetDot?1:0;
                    }
                    if (!isGetDot&&vector2Temp.dst(qipanDot[i - 1][j]) < 4f) {
                        isGetDot = j!=5;
                        i-=isGetDot?1:0;
                    }
                    //System.out.println("i= " + i + "  j= " + j);//待删除
                    if (!isGetDot&&vector2Temp.dst(qipanDot[i - 1][j - 1]) < 4f) {
                        isGetDot = j!=6;
                        i-=isGetDot?1:0;
                        j-=isGetDot?1:0;
                    }
                    out = false;

                }
            }
            if (isGetDot) System.out.println("qipanDOt: " + i + "  " + j);//待删除
            if (roundControl != -1) ;
            {
                if (isGetchess) {
                    if (roundControl == 1 && (chessArray.get(selecting).isCamp() == RB)) {
                        System.out.println("batch1");//待删除
                        //待删除
                        //System.out.println("selecting chess 3Dx= " +chessArray.get(selecting).transform.getTranslation(vector3Temp3).z+"  y= "+chessArray.get(selecting).transform.getTranslation(vector3Temp3).x);
                        //System.out.println("selecting chess x= " +chessArray.get(selecting).getQipanx()+"  y= "+chessArray.get(selecting).getQipany());
                        setSelected(selecting);

                        selecting = -1;
                        roundControl = 2;
                    } else {
                        if (roundControl == 1 && !(chessArray.get(selecting).isCamp() == RB)) {
                            //输出:你无法选择这个棋子
                        } else {
                            if (roundControl == 2 && (chessArray.get(selecting).isCamp() == RB)) {
                                //待删除
                                //System.out.println("selecting chess 3Dx= " +chessArray.get(selecting).transform.getTranslation(vector3Temp3).z+"  y= "+chessArray.get(selecting).transform.getTranslation(vector3Temp3).x);
                                //System.out.println("selecting chess x= " +chessArray.get(selecting).getQipanx()+"  y= "+chessArray.get(selecting).getQipany());
                                setSelected(selecting);
                                selecting = -1;
                                roundControl = 2;
                            } else if (roundControl == 2 && !(chessArray.get(selecting).isCamp() == RB)) {
                                if (chessArray.get(selected).takeqiJudge(chessArray.get(selecting), chessFenbu)) {
                                    roundControl = -1;
                                    takeChess(chessArray.get(selected), selecting);
                                    setSelected(-1);
                                    //发送信号:吃棋
                                    //调用将军提醒
                                } else {
                                    //你不能这么吃棋
                                }
                            }
                        }
                    }
                } else {
                    if (isGetDot && roundControl == 2) {
                        System.out.println("bath2");//待删除
                        if (chessArray.get(selected).zouqiJudge(i - 4, j - 5, chessFenbu)) {
                            System.out.println("bath3");//待删除
                            roundControl = -1;
                            System.out.println("x out="+(i-4)+"  y out="+(j-5));
                            chessArray.get(selected).createPathAnimation(i - 4, j - 5, chessFenbu);
                            setSelected(-1);
                            //发信服务器
                            //将军提醒
                        } else {
                            //输出,你的走棋不合法
                        }
                    }
                }
            }
            {//随机动画测试,待删除
           /* Random random = new Random();
            int randomx = random.nextInt(9) - 4;
            int randomy = random.nextInt(10) - 5;
            if (randomy >= 0) {
                randomy++;
            }
            chessArray.first().createPathAnimation(randomx, randomy,chessFenbu);*/
            }
        }
        return out;
    }

    public void setSelected (int value) {//若传入-1则去除所有的选棋
        if (selected == value) return;//选择棋子没变
        if (selected >= 0) {//返回原来的被选棋的效果
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
            selectionMaterial.set(ColorAttribute.createDiffuse(Color.GOLD));
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
