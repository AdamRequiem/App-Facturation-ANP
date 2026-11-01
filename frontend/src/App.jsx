import React, { useState, useEffect } from 'react';
import { Header } from './components/Header';
import { Footer } from './components/Footer';
import { InvoiceModal } from './components/InvoiceModal';
import { Dashboard } from './pages/Dashboard';
import { EscalesList } from './pages/EscalesList';
import { InvoiceGenerator } from './pages/InvoiceGenerator';
import { ShipsList } from './pages/ShipsList';
import { PortsList } from './pages/PortsList';
import { InvoiceHistory } from './pages/InvoiceHistory';
import { SettingsPage } from './pages/Settings';
import { api } from './services/api';

export function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [selectedFactureModal, setSelectedFactureModal] = useState(null);

  // App Data State
  const [navires, setNavires] = useState([]);
  const [ports, setPorts] = useState([]);
  const [escales, setEscales] = useState([]);
  const [factures, setFactures] = useState([]);
  const [tarifs, setTarifs] = useState([]);
  const [tarifTranches, setTarifTranches] = useState([]);
  const [marchandises, setMarchandises] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadAllData = async () => {
    setLoading(true);
    try {
      const [navRes, portRes, escRes, facRes, tarRes, trancheRes, marRes] = await Promise.all([
        api.getNavires(),
        api.getPorts(),
        api.getEscales(),
        api.getFactures(),
        api.getTarifs(),
        api.getTarifTranches(),
        api.getMarchandises()
      ]);

      setNavires(navRes || []);
      setPorts(portRes || []);
      setEscales(escRes || []);
      setFactures(facRes || []);
      setTarifs(tarRes || []);
      setTarifTranches(trancheRes || []);
      setMarchandises(marRes || []);
    } catch (e) {
      console.error('Error loading API data:', e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAllData();
  }, []);

  const handleInvoiceGenerated = async (newFacture) => {
    await loadAllData(); // resynchronise escales/factures/touchées (le brouillon n'apparaîtra pas dans l'historique/dashboard, filtré par statut)
    setSelectedFactureModal(newFacture); // ouvre la facture brouillon pour validation
  };

  const handleValiderFacture = async (idEnteteFacture) => {
    const validee = await api.validerFacture(idEnteteFacture);
    setSelectedFactureModal(validee); // le modal reflète immédiatement le statut VALIDEE
    await loadAllData(); // la facture apparaît maintenant dans historique/dashboard
    return validee;
  };

  // Seules les factures validées comptent pour le dashboard et l'historique —
  // un brouillon généré mais pas encore validé reste invisible partout ailleurs que dans son propre modal
  const facturesValidees = factures.filter(f => f.statut !== 'BROUILLON');

  return (
    <div className="app-container">
      {/* Header Bar */}
      <Header activeTab={activeTab} setActiveTab={setActiveTab} />

      {/* Main Page Content */}
      <main className="main-content">
        {loading && factures.length === 0 ? (
          <div style={{ textAlign: 'center', padding: '4rem 1rem', color: 'var(--anp-navy)' }}>
            <h2>Chargement du Système ANP...</h2>
            <p>Synchronisation avec la base de données portuaire</p>
          </div>
        ) : (
          <>
            {activeTab === 'dashboard' && (
              <Dashboard
                navires={navires}
                factures={facturesValidees}
                ports={ports}
                setActiveTab={setActiveTab}
                onOpenInvoiceDetails={(f) => setSelectedFactureModal(f)}
              />
            )}

            {activeTab === 'escales' && (
              <EscalesList
                escales={escales}
                navires={navires}
                ports={ports}
                marchandises={marchandises}
                onRefresh={loadAllData}
              />
            )}

            {activeTab === 'generator' && (
              <InvoiceGenerator
                escales={escales}
                onInvoiceGenerated={handleInvoiceGenerated}
              />
            )}

            {activeTab === 'ships' && (
              <ShipsList
                navires={navires}
                onRefresh={loadAllData}
              />
            )}

            {activeTab === 'ports' && (
              <PortsList
                ports={ports}
                onRefresh={loadAllData}
              />
            )}

            {activeTab === 'history' && (
              <InvoiceHistory
                factures={facturesValidees}
                onRefresh={loadAllData}
                onOpenInvoiceDetails={(f) => setSelectedFactureModal(f)}
              />
            )}

            {activeTab === 'settings' && (
              <SettingsPage
                tarifTranches={tarifTranches}
                tarifs={tarifs}
                ports={ports}
                onRefresh={loadAllData}
              />
            )}
          </>
        )}
      </main>

      {/* Footer */}
      <Footer />

      {/* Official Printable Invoice Modal */}
      {selectedFactureModal && (
        <InvoiceModal
          facture={selectedFactureModal}
          onClose={() => setSelectedFactureModal(null)}
          onValidate={handleValiderFacture}
        />
      )}
    </div>
  );
}

export default App;
