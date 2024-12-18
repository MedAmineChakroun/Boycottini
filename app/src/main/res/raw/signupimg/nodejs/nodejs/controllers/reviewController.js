import Review from '../models/review.js';

export const createReview = async (req, res, next) => {
    try {
        const newReview = new Review(req.body);
        await newReview.save();
        res.status(201).json(newReview);
    } catch (error) {
        next(error);
    }
};

export const getReviews = async (req, res, next) => {
    try {
        const reviews = await Review.find().populate('itemId').populate('userId');
        res.json(reviews);
    } catch (error) {
        next(error);
    }
};

export const getReviewsByUserId = async (req, res, next) => {
    try {
        const reviews = await Review.find({ userId: req.params.userId }).populate('itemId');
        res.json(reviews);
    } catch (error) {
        next(error);
    }
};

export const getReviewsByItemId = async (req, res, next) => {
    try {
        const reviews = await Review.find({ itemId: req.params.itemId })
            .populate('itemId')  
            .populate('userId', 'name race'); 
        res.json(reviews);
    } catch (error) {
        next(error);
    }
};

