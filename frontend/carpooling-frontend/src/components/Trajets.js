export default function Trajets() {
    const trajets = []; // temporairement vide

    return (
        <section className="py-16 bg-gray-50">
            <h2 className="text-3xl font-bold text-center text-purple-700 mb-8">
                Trajets disponibles
            </h2>

            {trajets.length === 0 ? (
                <p className="text-center text-gray-500">Aucun trajet disponible pour le moment.</p>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6 px-6">
                    {trajets.map((t, i) => (
                        <div key={i} className="bg-white rounded-xl shadow-md p-6">
                            {}
                        </div>
                    ))}
                </div>
            )}
        </section>
    );
}
