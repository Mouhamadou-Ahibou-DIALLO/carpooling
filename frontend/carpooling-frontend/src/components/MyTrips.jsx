import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import MyPublishedTrips from '@/components/MyPublishedTrips';
import MyBookings from '@/components/MyBookings';

const MyTrips = () => {
    return (
        <section className="container mx-auto px-4 py-16">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
            >
                <h2 className="text-4xl font-bold mb-8 gradient-text">
                    Mes trajets
                </h2>

                <Tabs defaultValue="bookings" className="w-full">
                    <TabsList className="grid w-full max-w-md grid-cols-2 mb-8">
                        <TabsTrigger value="bookings">Mes réservations</TabsTrigger>
                        <TabsTrigger value="published">Mes publications</TabsTrigger>
                    </TabsList>

                    <TabsContent value="bookings">
                        <MyBookings />
                    </TabsContent>

                    <TabsContent value="published">
                        <MyPublishedTrips />
                    </TabsContent>
                </Tabs>
            </motion.div>
        </section>
    );
};

export default MyTrips;