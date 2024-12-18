import mongoose from 'mongoose'; 
 
const itemSchema = new mongoose.Schema({ 
    name: { 
        type: String, 
        required: true 
    }, 
    price: { 
        type: Number, 
        required: true 
    }, 
    description: String 
}); 
 
const Item = mongoose.model('Item', itemSchema); 
 
export default Item;