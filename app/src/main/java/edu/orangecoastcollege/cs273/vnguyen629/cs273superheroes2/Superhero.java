package edu.orangecoastcollege.cs273.vnguyen629.cs273superheroes2;

/**
 * The Superhero class is a place holder for data corresponding to CS273 Superheroes
 * including their user names, names, super powers, one thing, and corresponding image.
 *
 * @author Vincent Nguyen
 */
public class Superhero {
    private String mUserName;
    private String mName;
    private String mSuperPower;
    private String mOneThing;
    private String mImageName;

    /**
     * Creates a superhero initializing the user name, name
     * superpower, one thing, and image name to empty/blank
     * values.
     */
    public Superhero() {
        mUserName = "";
        mName = "";
        mSuperPower = "";
        mOneThing = "";
        mImageName = "";
    }

    /**
     * Creates a superhero based on the parameters given assigning
     * the superheroes' user name, name, superpower, one thing,
     * and image name.
     * @param newUserName
     * @param newName
     * @param newSuperPower
     * @param newOneThing
     * @param newImageName
     */
    public Superhero(String newUserName, String newName, String newSuperPower,
                     String newOneThing, String newImageName) {
        mUserName = newUserName;
        mName = newName;
        mSuperPower = newSuperPower;
        mOneThing = newOneThing;
        mImageName = newImageName;
    }

    /**
     * Gets/returns the username of the superhero
     * @return Superheroes' current user name
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * Gets/returns the name of the superhero
     * @return Superheroes' current name
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets/returns the superpower of the hero
     * @return Superheroes' current superpower
     */
    public String getSuperPower() {
        return mSuperPower;
    }

    /**
     * Gets/returns the one thing of the superhero
     * @return Superheroes' one thing
     */
    public String getOneThing() {
        return mOneThing;
    }

    /**
     * Gets/returns the name of the image of the superhero
     * @return Superheroes' current image
     */
    public String getImageName() {
        return mImageName;
    }

    /**
     * Sets/changes the current user name of the superhero
     * @param newUserName
     */
    public void setUserName(String newUserName) {
        mUserName = newUserName;
    }

    /**
     * Sets/changes the current name of the superhero
     * @param newName
     */
    public void setName(String newName) {
        mName = newName;
    }

    /**
     * Sets/changes the current superpower of the hero
     * @param newSuperPower
     */
    public void setSuperPower(String newSuperPower) {
        mSuperPower = newSuperPower;
    }

    /**
     * Sets/changes the one thing of the hero
     * @param newOneThing
     */
    public void setOneThing(String newOneThing) {
        mOneThing = newOneThing;
    }

    /**
     * Sets/changes the currnet image name of the hero
     * @param newImageName
     */
    public void setImageName(String newImageName) {
        mImageName = newImageName;
    }
}