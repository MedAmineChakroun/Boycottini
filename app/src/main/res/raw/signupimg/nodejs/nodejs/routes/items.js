import express from 'express'; 
import { createItem, getItems, getItemById, updateItem, deleteItem } 
from '../controllers/itemController.js'; 
const router = express.Router(); 
// Route for creating a new item 
router.post('/', createItem); 
// Route for fetching all items 
router.get('/', getItems); 
// Route for fetching an item by ID 
router.get('/:id', getItemById); 
// Route for updating an item by ID 
router.put('/:id', updateItem); 
// Route for deleting an item by ID 
router.delete('/:id', deleteItem); 
export default router;