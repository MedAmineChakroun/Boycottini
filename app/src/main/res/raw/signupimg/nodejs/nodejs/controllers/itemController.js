import Item from '../models/item.js'; 
 
// Create a new item 
export const createItem = async (req, res, next) => { 
    try { 
        const newItem = new Item(req.body); 
        await newItem.save(); 
        res.status(201).json(newItem); 
    } catch (error) { 
        next(error); 
    } 
}; 
 
// Get all items 
export const getItems = async (req, res, next) => { 
    try { 
        const items = await Item.find(); 
        res.json(items); 
    } catch (error) { 
        next(error); 
    } 
}; 

export const getItemById = async (req, res, next) => { 
    try { 
        const item = await Item.findById(req.params.id); 
        if (!item) { 
            return res.status(404).json({ message: 'Item not found' 
}); 
        } 
        res.json(item); 
    } catch (error) { 
        next(error); 
    } 
}; 
 
// Update an item by ID 
export const updateItem = async (req, res, next) => { 
    try { 
        const updatedItem = await 
Item.findByIdAndUpdate(req.params.id, req.body, { new: true }); 
        if (!updatedItem) { 
            return res.status(404).json({ message: 'Item not found' 
}); 
        } 
        res.json(updatedItem); 
    } catch (error) { 
        next(error); 
    } 
}; 
 
// Delete an item by ID 
export const deleteItem = async (req, res, next) => { 
    try { 
        const deletedItem = await 
Item.findByIdAndDelete(req.params.id); 
        if (!deletedItem) { 
            return res.status(404).json({ message: 'Item not found' 
}); 
        } 
        res.json({ message: 'Item deleted' }); 
    } catch (error) { 
        next(error); 
    } 
}; 