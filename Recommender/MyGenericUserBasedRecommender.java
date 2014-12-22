package com.recommand.RecommanderApp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class MyGenericUserBasedRecommender {
    public static void main( String[] args ) throws IOException, TasteException
    {
     // Create a data source from the CSV file
    File userPreferencesFile = new File("data/dataset.csv");
    DataModel dataModel = new FileDataModel(userPreferencesFile);
    
    UserSimilarity userSimilarity = new EuclideanDistanceSimilarity(dataModel);
    UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(2, userSimilarity, dataModel);
    Recommender genericRecommender =  new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);

    // Recommend 5 items for each user
    for (LongPrimitiveIterator iterator = dataModel.getUserIDs(); iterator.hasNext();)
    {
        long userId = iterator.nextLong();

        // Generate a list of 5 recommendations for the user
        List<RecommendedItem> itemRecommendations = genericRecommender.recommend(userId, 5);

        System.out.format("User Id: %d%n", userId);

        if (itemRecommendations.isEmpty())
        {
            System.out.println("No recommendations for this user.");
        }
        else
        {
            // Display the list of recommendations
            for (RecommendedItem recommendedItem : itemRecommendations)
            {
                System.out.format("Recommened Item Id %d. Strength of the preference: %f%n", recommendedItem.getItemID(), recommendedItem.getValue());
            }
        }
    }
    }
}
