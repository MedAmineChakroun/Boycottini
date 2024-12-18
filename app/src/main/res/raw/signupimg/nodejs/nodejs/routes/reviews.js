import express from 'express';
import { createReview, getReviews, getReviewsByUserId,getReviewsByItemId } from '../controllers/reviewController.js';

const router = express.Router();

router.post('/', createReview);
router.get('/', getReviews);
router.get('/user/:userId', getReviewsByUserId);
router.get('/item/:itemId', getReviewsByItemId);


export default router;
