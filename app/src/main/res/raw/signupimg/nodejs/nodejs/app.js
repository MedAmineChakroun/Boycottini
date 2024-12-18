import express from 'express'; 
import mongoose from 'mongoose'; 
import itemRoutes from './routes/items.js'; 
import userRoutes from './routes/users.js';
import reviewRoutes from './routes/reviews.js';
import cors from 'cors';

const app = express(); 
const PORT = process.env.PORT || 3000; 
 
app.use(express.json()); 

app.use(cors({
  origin: 'http://localhost:4200',  
  methods: ['GET', 'POST', 'DELETE'],  
  allowedHeaders: ['Content-Type', 'Authorization'], 
}));
 
app.use('/api/items', itemRoutes); 
app.use('/api/users', userRoutes);
app.use('/api/reviews', reviewRoutes);

mongoose.connect('mongodb://localhost:27017/orianss') 
    .then(() => console.log('Connected to MongoDB')) 
    .catch(err => console.error('Failed to connect to MongoDB',
err)); 
 

app.listen(PORT, () => { 
  console.log(`Server running on port ${PORT}`); 
}); 
export default app;