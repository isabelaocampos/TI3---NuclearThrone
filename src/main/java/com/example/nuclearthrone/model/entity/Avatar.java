package com.example.nuclearthrone.model.entity;

import java.util.HashMap;

import com.example.nuclearthrone.MainMenu;
import com.example.nuclearthrone.model.KeyboardControl;
import com.example.nuclearthrone.model.entity.item.Healing;
import com.example.nuclearthrone.model.entity.item.Item;
import com.example.nuclearthrone.model.entity.item.Weapon;
import com.example.nuclearthrone.model.level.Level;
import com.example.nuclearthrone.model.util.Direction;
import com.example.nuclearthrone.model.util.Images;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Avatar extends Entity implements IAnimation {

    public static final int HEALTH = 100;
    public static final double SPEED = 3;
    public static final double WIDTH = 50;
    public static final double HEIGHT = 50;

    private static HashMap<AnimationType, Image[]> animations;
    private static AnimationType animation;
    private static Direction lookingAt;
    private static Timeline animationPlayer;
    private static int spriteStage = 0;
    private static Avatar instance;
    private static final BooleanBinding keyPressed = KeyboardControl.wPressed.or(KeyboardControl.aPressed).or(
            KeyboardControl.sPressed).or(KeyboardControl.dPressed);
    public static ImageView[] hearts;
    public static ImageView hand;
    public static ProgressBar reloadBar;

    private int state;

    public static Avatar getInstance() {
        if (instance == null) {
            instance = new Avatar(200, 70, WIDTH, HEIGHT);
            updateLifeBar();
        }
        return instance;
    }

    public Weapon weapon;

    private Avatar(double x, double y, double width, double height) {
        super(x, y, width, height, HEALTH, true);
        keyPressed.addListener(this::onKeyPressed);
        animation = AnimationType.IDLE;
        lookingAt = Direction.RIGHT;
        isAlive = true;
        initAnimation();
        animationPlayer = getAnimation();
        startAnimation();
        hand.setImage(new Image(MainMenu.getFile("entities/weapon/pixelFist.png").getPath()));
    }


    public void attack(double x, double y) {
        if (/*animation != AnimationType.SHOOT && animation != AnimationType.ATTACK &&*/ isAlive && weapon != null) {
            if (x > getX()) {
                lookingAt = Direction.RIGHT;
            } else {
                lookingAt = Direction.LEFT;
            }
            reloadBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            weapon.attack(x, y);
            spriteStage = 0;
            animation = AnimationType.ATTACK;
            startAnimation();
        }
    }


    public Item collect(Item item) {
        Item temp = null;
        if (item instanceof Weapon) {
            if (weapon == null) {
                weapon = (Weapon) item;
            } else {
                temp = weapon;
                weapon = (Weapon) item;
                temp.setX(weapon.getX());
                temp.setY(weapon.getY());
            }
            hand.setImage(weapon.sprite);
        }
        if(item instanceof Healing){
            if(instance.health == 100){
                return item;
            }
            instance.health += Healing.HEAL_AMOUNT;
            if(instance.health > 100){
                instance.health = 100;
            }
            updateLifeBar();
        }
        return temp;
    }

    public void collectNearbyItem() {
        for (int i = 0; i < Level.currentLevel().items.size(); i++) {
            Item current = Level.currentLevel().items.get(i);
            if (current.intersects(instance)) {
                Item dropped = instance.collect(current);
                if (dropped != null) {
                    Level.currentLevel().items.set(i, dropped);
                } else {
                    Level.currentLevel().items.remove(i);
                }
                break;
            }
        }
    }

    @Override
    public void takeDamage(Entity other) {
        if (isAlive) {
            animation = AnimationType.HIT;
            health -= other.damage;
            isAlive = health > 0;

            if (!isAlive) {
                animation = AnimationType.DEATH;
            }
            spriteStage = 0;
            updateLifeBar();
        }
    }

    // utiliza para representar un valor observable.
    // Proporciona una forma de observar y recibir
    // notificaciones cuando el valor cambia.
    public void onKeyPressed(ObservableValue<? extends Boolean> observable, Boolean a, Boolean keyPressed) {
        if (/*animation != AnimationType.SHOOT && animation != AnimationType.ATTACK && */ isAlive) {
            if (keyPressed) {
                movement.start();
                if(animation != AnimationType.HIT){
                    animation = AnimationType.RUN;
                }
            } else {
                movement.stop();

                if(animation != AnimationType.HIT){
                animation = AnimationType.IDLE;
                }
            }
            if(animation != AnimationType.HIT){
                spriteStage = 0;
            }
        }
    }

    AnimationTimer movement = new AnimationTimer() {
        @Override
        public void handle(long timestamp) {
            if (animation != AnimationType.SHOOT && animation != AnimationType.ATTACK && isAlive) {
                double previousX = getX();
                double previousY = getY();
                if (KeyboardControl.wPressed.get()) {
                    setY(previousY - SPEED);
                }
                if (KeyboardControl.sPressed.get()) {
                    setY(previousY + SPEED);
                }
                if (KeyboardControl.aPressed.get()) {
                    setX(previousX - SPEED);
                    lookingAt = Direction.LEFT;
                }
                if (KeyboardControl.dPressed.get()) {
                    setX(previousX + SPEED);
                    lookingAt = Direction.RIGHT;
                }


                if (isOutOfScreen(instance)) {
                    if (!Level.inGate(instance)) {
                        setX(previousX);
                        setY(previousY);
                    }
                }

                for (Entity entity : Level.currentLevel().walls) {
                    if (intersects(entity)) {
                        setX(previousX);
                        setY(previousY);
                    }
                }

                if ((getX() != previousX || getY() != previousY) && animation != AnimationType.RUN && animation != AnimationType.HIT) {
                    animation = AnimationType.RUN;
                    spriteStage = 0;
                }
            }
        }
    };

    @Override
    public void startAnimation() {
        animationPlayer.setCycleCount(Animation.INDEFINITE);
        animationPlayer.play();
    }

    @Override
    public void stopAnimation() {
        animationPlayer.stop();
    }

    @Override
    public Timeline getAnimation() {
        return new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            if (spriteStage < animations.get(animation).length - 1) {
                spriteStage++;
            } else {
                if (animation != AnimationType.DEATH) {
                    if (animation != AnimationType.IDLE && animation != AnimationType.RUN) {
                        animation = AnimationType.IDLE;
                    }
                    spriteStage = 0;
                } else {
                    stopAnimation();
                }
            }
            sprite = animations.get(animation)[spriteStage];
            if (lookingAt == Direction.LEFT)
                sprite = Images.mirrorImageHorizontally(sprite);
        }));
    }

    public void initAnimation() {

        this.state = 0;

        if(animations == null){
            animations = new HashMap<>();

            //** /
            animations.put(AnimationType.IDLE, new Image[8]);
            for (int i = 1; i <= 8; i++) {
                String uri = "file:" + MainMenu.getFile("entities/animations/PNG/idle/idle_" + i + ".png").getPath();
                animations.get(AnimationType.IDLE)[i - 1] = new Image(uri, WIDTH,HEIGHT, false, true, false);
            }

            //* */
            animations.put(AnimationType.RUN, new Image[10]);
            for (int i = 1; i <= 10; i++) {
                String uri = "file:" + MainMenu.getFile("entities/animations/PNG/run/run_" + i + ".png").getPath();
                animations.get(AnimationType.RUN)[i - 1] = new Image(uri,WIDTH,HEIGHT,false, true, false);
            }

            //* pondré 2_attack porque parece ser el que dispara/
            animations.put(AnimationType.SHOOT, new Image[15]);
            for (int i = 1; i <= 15; i++) {
                String uri = "file:" + MainMenu.getFile("entities/animations/PNG/2_atk/2_atk_" + i + ".png").getPath();
                animations.get(AnimationType.SHOOT)[i - 1] = new Image(uri, WIDTH,HEIGHT, false, true, false);
            }

            //* es golpeado el jugadorrrrr */
            animations.put(AnimationType.HIT, new Image[19]);
            for (int i = 1; i <= 19; i++) {
                String uri = "file:" + MainMenu.getFile("entities/animations/PNG/defend/defend_" + i+ ".png").getPath();
                animations.get(AnimationType.HIT)[i - 1] = new Image(uri,WIDTH,HEIGHT,false, true, false);
            }

            //* */
            animations.put(AnimationType.DEATH, new Image[19]);
            for (int i = 1; i <= 19; i++) {
                String uri = "file:" + MainMenu.getFile("entities/animations/PNG/death/death_"+ i + ".png").getPath();
                animations.get(AnimationType.DEATH)[i - 1] = new Image(uri,WIDTH,HEIGHT, true, false, false);
            }

            //** uso attack 2, no muy presente pq ya*/
            animations.put(AnimationType.ATTACK, new Image[15]);
            for (int i = 1; i <= 15; i++) {
                String uri = "file:" + MainMenu.getFile("entities/animations/PNG/2_atk/2_atk_" + i + ".png").getPath();
                animations.get(AnimationType.ATTACK)[i - 1] = new Image(uri, WIDTH,HEIGHT,false, true, false);
            }
        }
    }

    private static void updateLifeBar() {
        int nHearts = hearts.length;
        for (int i = 0; i < nHearts; i++) {
            if((i+1)*30 < instance.health){
                hearts[i].setImage(new Image(MainMenu.getFile("windows/full-heart.png").getPath()));
            }else{
                hearts[i].setImage(new Image(MainMenu.getFile("windows/empty-heart.png").getPath()));
            }
        }
    }

    public static void resetAvatar(){
        instance.stopAnimation();
        instance = null;
    }
}
